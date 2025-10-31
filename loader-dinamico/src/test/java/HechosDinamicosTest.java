import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO_OUT;
import dinamico.models.entidades.hecho.HechoCrudo;
import dinamico.models.repositorios.IRepositorioHechosCrudos;
import dinamico.service.implementacion.HechosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HechosDinamicosTest {

    @Mock
    private IRepositorioHechosCrudos repositorioHechosCrudos;

    @InjectMocks
    private HechosService service;

    //  para simular la inyección de @Value
    private final String NOMBRE_FUENTE_TEST = "Nombre Fuente Dinamica";

    @BeforeEach
    void setUp() {
        // Inyecta el valor simulado en el campo anotado con @Value
        ReflectionTestUtils.setField(service, "nombreFuente", NOMBRE_FUENTE_TEST);
    }

    private HechoCrudoDTO_IN dtoInBasico() {
        var dto = new HechoCrudoDTO_IN();
        dto.setTitulo("Inundación en Zona Sur");
        dto.setDescripcion("Muchas calles inundadas");
        dto.setLatitud("10.0");
        dto.setLongitud("20.0");
        dto.setFechaAcontecimiento(LocalDateTime.now().minusDays(1).toString());
        dto.setCategoria("Inundacion");
        dto.setContribuyenteID("1234");
        dto.setEtiqueta("Inundacion");
        dto.setArchivosMultimedia(Collections.emptyList());
        return dto;
    }

    private HechoCrudo hechoCrudoNoLeido() {
        HechoCrudo hecho = new HechoCrudo(
                "Hecho Nuevo",
                "Descripción",
                "Cat",
                "10.0",
                "20.0",
                LocalDateTime.now().toString(),
                "Etiqueta",
                "1234",
                List.of("url")
        );
        hecho.setFueLeido(false);
        return hecho;
    }

    @Test
    void cargarHecho_casoExitoso() {
        var dto = dtoInBasico();
        ArgumentCaptor<HechoCrudo> captor = ArgumentCaptor.forClass(HechoCrudo.class);

        service.cargarHecho(dto);

        verify(repositorioHechosCrudos, times(1)).save(captor.capture());

        HechoCrudo hechoGuardado = captor.getValue();
        assertThat(hechoGuardado.getTitulo()).isEqualTo("Inundación en Zona Sur");
        assertThat(hechoGuardado.getFueLeido()).isFalse(); //verifico esto porque así tendría que quedar por defecto
    }

    // tests para obtenerHechos()
    @Test
    void obtenerHechos_conNuevosHechos_marcaComoLeidoYRetorna() {
        HechoCrudo h1 = hechoCrudoNoLeido();
        h1.setTitulo("Hecho 1");
        HechoCrudo h2 = hechoCrudoNoLeido();
        h2.setTitulo("Hecho 2");
        List<HechoCrudo> hechosNoLeidos = List.of(h1, h2);


        when(repositorioHechosCrudos.findByFueLeidoFalse()).thenReturn(hechosNoLeidos);

        List<HechoCrudoDTO_OUT> result = service.obtenerHechos();

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(repositorioHechosCrudos, times(1)).saveAll(captor.capture());

        // Verifica que los hechos en el captor están marcados como leídos
        List<HechoCrudo> hechosGuardados = captor.getValue();
        assertThat(hechosGuardados).allMatch(HechoCrudo::getFueLeido);

        // Verifica el DTO de Salida
        assertThat(result).hasSize(2);
        assertThat(result).extracting("titulo").containsExactly("Hecho 1", "Hecho 2");

        // Verifica la inyección del nombre de la fuente de la configuración
        assertThat(result).extracting("origen").containsExactly(NOMBRE_FUENTE_TEST, NOMBRE_FUENTE_TEST);
        assertThat(result.get(0).getTipoFuente()).isEqualTo("DINAMICA");
    }

    @Test
    void obtenerHechos_sinNuevosHechos_retornaListaVacia() {

        when(repositorioHechosCrudos.findByFueLeidoFalse()).thenReturn(Collections.emptyList());

        List<HechoCrudoDTO_OUT> result = service.obtenerHechos();

        assertThat(result).isNotNull().isEmpty();

        //verifico que findByFueLeidoFalse solo se llamó una vez y que el saveAll se llama igual aunque no hayan hechos para guardar (así está el método)--> "guarda" lista vacía
        verify(repositorioHechosCrudos, times(1)).findByFueLeidoFalse();
        verify(repositorioHechosCrudos, times(1)).saveAll(Collections.emptyList());
    }

    @Test
    void obtenerHechos_errorEnRepositorio_lanzaRuntimeException() {

        when(repositorioHechosCrudos.findByFueLeidoFalse()).thenThrow(new RuntimeException("Error de BD simulado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerHechos();
        });

        assertThat(exception.getMessage()).contains("Error al obtener los hechos del loader dinámico");
        verify(repositorioHechosCrudos, times(1)).findByFueLeidoFalse();
        verify(repositorioHechosCrudos, never()).saveAll(anyList());
    }
}

