import org.example.metamapa.estatico.models.dtos.HechoDTO;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.example.metamapa.estatico.models.repositorios.IRepositorioHechos;
import org.example.metamapa.estatico.service.implementaciones.HechosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObtenerHechosTest {

    @Mock
    private IRepositorioHechos repositorioHechos;

    @InjectMocks
    private HechosService service;

    private final String LOADER_ID = "001";

    @BeforeEach
    void setUp() {
        // Simular la inyección de @Value("${loader.self.id}")
        ReflectionTestUtils.setField(service, "loaderId", LOADER_ID);
    }

    private HechoCrudo crearHechoCrudoNoEnviado(String titulo) {
        // para simular un hecho nuevo cargado
            HechoCrudo hecho = new HechoCrudo(titulo, "Desc", "Cat", "10", "20", "2025-01-01");
        hecho.setLoaderId(LOADER_ID);
        return hecho;
    }

    @Test
    void obtenerHechos_conNuevosHechos_marcaYRetorna() {

        HechoCrudo h1 = crearHechoCrudoNoEnviado("Evento A");
        HechoCrudo h2 = crearHechoCrudoNoEnviado("Evento B");
        List<HechoCrudo> hechosNoEnviados = List.of(h1, h2);

        when(repositorioHechos.findByEnviadoFalseAndLoaderId(LOADER_ID))
                .thenReturn(hechosNoEnviados);
        // para verificar qué se guardó
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        List<HechoDTO> result = service.obtenerHechos();

        verify(repositorioHechos, times(1)).findByEnviadoFalseAndLoaderId(LOADER_ID);
        verify(repositorioHechos, times(1)).saveAll(captor.capture()); // tiene q guardar los hechos modificados

        List<HechoCrudo> hechosGuardados = captor.getValue();
        assertThat(hechosGuardados).hasSize(2);

        // chequeo que se haya cambiado el bool de enviado y se puso la fecha del envío
        assertThat(hechosGuardados).allMatch(HechoCrudo::isEnviado);
        assertThat(hechosGuardados).allMatch(h -> h.getFechaEnvio() != null);

        assertThat(result).hasSize(2);
        assertThat(result).extracting("titulo").containsExactly("Evento A", "Evento B");
        assertThat(result.get(0).getTipoFuente()).isEqualTo("ESTATICA");
    }

    @Test
    void obtenerHechos_sinNuevosHechos_retornaVacio() {

        when(repositorioHechos.findByEnviadoFalseAndLoaderId(LOADER_ID))
                .thenReturn(Collections.emptyList());

        List<HechoDTO> result = service.obtenerHechos();

        assertThat(result).isNotNull().isEmpty();

        verify(repositorioHechos, times(1)).findByEnviadoFalseAndLoaderId(LOADER_ID);
        verify(repositorioHechos, never()).saveAll(anyList());
    }
}
