import org.example.metamapa.gestordatos.Servicios.Implementaciones.HechoService;
import org.example.metamapa.gestordatos.Servicios.Implementaciones.FiltradorService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.*;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.repositorios.ICategoriaRepository;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class HechosPublicosTest {


    @Mock private IHechosRepository hechosRepo;
    @Mock private FiltradorService filtradorService;
    @Mock private ICategoriaRepository categoriaRepo;
    @InjectMocks private HechoService service;


    //Buscar todos los hechos

    @Test
    void buscarTodosLosHechos_sinCriterios() {
        List<Hecho> hechos = List.of(new Hecho(), new Hecho());
        when(hechosRepo.findAll()).thenReturn(hechos);

        List<HechoOutputDTO> out = service.buscarTodosLosHechos(Collections.emptyList());

        assertThat(out).hasSize(2);
        verify(hechosRepo).findAll();
        verifyNoInteractions(filtradorService);
    }

    @Test
    void buscarTodosLosHechos_conCriterios() {
        var criterio = new CriterioRequest();
        criterio.setTipo("porCategoria");
        criterio.setParams(Map.of("categoriaDeseada", "Incendios"));
        List<CriterioRequest> criterios = List.of(criterio);

        List<Hecho> hechosFiltrados = List.of(new Hecho());
        when(filtradorService.filtrarHechosDataBase(anyList())).thenReturn(hechosFiltrados);

        try (MockedStatic<StringAObjetos> st = mockStatic(StringAObjetos.class)) {
            st.when(() -> StringAObjetos.criterioFactory(any())).thenReturn(mock(CondicionDeFiltrado.class));

            List<HechoOutputDTO> out = service.buscarTodosLosHechos(criterios);

            assertThat(out).hasSize(1);
            verify(filtradorService).filtrarHechosDataBase(anyList());
        }
    }


    //Editar hecho contribuyente
    @Test
    void editarHecho_noExiste() {
        when(hechosRepo.findById(99L)).thenReturn(Optional.empty());

        boolean ok = service.editarHechoContribuyente(99L, Map.of("titulo", "Nuevo"));

        assertThat(ok).isFalse();
        verify(hechosRepo).findById(99L);
        verifyNoMoreInteractions(hechosRepo);

    }

    @Test
    void editarHecho_sinContribuyente() {
        Hecho hecho = new Hecho();
        when(hechosRepo.findById(1L)).thenReturn(Optional.of(hecho));

        boolean ok = service.editarHechoContribuyente(1L, Map.of("titulo", "Nuevo"));

        assertThat(ok).isFalse();
        verify(hechosRepo, never()).save(any());
    }

    @Test
    void editarHecho_contribuyenteValido() {
        ContribuyenteRegistrado contrib = new ContribuyenteRegistrado();
        contrib.setUserId(1L);
        Hecho hecho = mock(Hecho.class);
        when(hechosRepo.findById(1L)).thenReturn(Optional.of(hecho));
        when(hecho.getContribuyente()).thenReturn(contrib);

        doNothing().when(hecho).editarCon(any(Hecho.class), eq(contrib));

        boolean ok = service.editarHechoContribuyente(1L, Map.of("titulo", "Actualizado"));

        assertThat(ok).isTrue();
        verify(hecho).editarCon(any(Hecho.class), eq(contrib));
        verify(hechosRepo).save(hecho);
    }

    @Test
    void editarHecho_contribuyenteValido_noAutorizado() {
        ContribuyenteRegistrado contrib = new ContribuyenteRegistrado();
        contrib.setUserId(1L);

        Hecho hecho = mock(Hecho.class);
        when(hechosRepo.findById(2L)).thenReturn(Optional.of(hecho));
        when(hecho.getContribuyente()).thenReturn(contrib);

        // fuera de los 7 dias
        doThrow(new IllegalStateException("Fuera de plazo")).when(hecho).editarCon(any(), eq(contrib));

        boolean ok = service.editarHechoContribuyente(2L, Map.of("titulo", "Intento"));

        assertThat(ok).isFalse();
        verify(hechosRepo, never()).save(any());
    }

    @Test
    void editarHecho_cambiaCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Desastres");
        ContribuyenteRegistrado contrib = new ContribuyenteRegistrado();
        contrib.setUserId(1L);
        Hecho hecho = mock(Hecho.class);
        when(hechosRepo.findById(3L)).thenReturn(Optional.of(hecho));
        when(hecho.getContribuyente()).thenReturn(contrib);
        when(categoriaRepo.findByNombreIgnoreCase("Desastres")).thenReturn(Optional.of(categoria));

        doNothing().when(hecho).editarCon(any(Hecho.class), eq(contrib));

        Map<String, Object> cambios = Map.of("categoria", "Desastres");
        boolean ok = service.editarHechoContribuyente(3L, cambios);

        assertThat(ok).isTrue();
        verify(categoriaRepo).findByNombreIgnoreCase("Desastres");
        verify(hechosRepo).save(hecho);
    }
}
