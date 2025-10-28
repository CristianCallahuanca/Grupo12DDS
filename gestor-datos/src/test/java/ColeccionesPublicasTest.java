import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.Servicios.Implementaciones.ColeccionesService;
import org.example.metamapa.gestordatos.Servicios.Implementaciones.FiltradorService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.ModosNavegacion.ModoNavegacion;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.repositorios.IColeccionesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ColeccionesPublicasTest {

    @Mock private IColeccionesRepository coleccionesRepository;
    @Mock private IHechoService hechoService;
    @Mock private FiltradorService filtradorService;

    @InjectMocks
    private ColeccionesService service;

    private Coleccion coleccion(String handle, String titulo, String desc, List<String> origenes) {
        Coleccion c = new Coleccion(handle, null, new ArrayList<>(), titulo, desc, new ArrayList<>(), null);
        c.setOrigenesReales(origenes != null ? new ArrayList<>(origenes) : new ArrayList<>());
        return c;
    }

    @Test
    void retrieveColecciones() {
        var c1 = coleccion("h1", "T1", "D1", List.of("Fuente-A"));
        var c2 = coleccion("h2", "T2", "D2", List.of("Fuente-B"));

        when(coleccionesRepository.findAll()).thenReturn(List.of(c1, c2));

        List<ColeccionOutputDTO> out = service.retrieveColecciones();

        assertThat(out).hasSize(2);
        assertThat(out).extracting(ColeccionOutputDTO::getHandle).containsExactly("h1", "h2");
        assertThat(out).extracting(ColeccionOutputDTO::getTitulo).containsExactly("T1", "T2");
        assertThat(out.get(0).getOrigenesReales()).containsExactly("Fuente-A");
        assertThat(out.get(1).getOrigenesReales()).containsExactly("Fuente-B");
        verify(coleccionesRepository).findAll();
    }


    @Test
    void retrieveColeccion_existente() {
        var c = coleccion("abc", "Colec ABC", "Desc", List.of("Fuente-X"));
        when(coleccionesRepository.findById("abc")).thenReturn(Optional.of(c));

        ColeccionOutputDTO out = service.retrieveColeccion("abc");

        assertThat(out).isNotNull();
        assertThat(out.getHandle()).isEqualTo("abc");
        assertThat(out.getTitulo()).isEqualTo("Colec ABC");
        verify(coleccionesRepository).findById("abc");
    }

    @Test
    void retrieveColeccion_inexistente() {
        when(coleccionesRepository.findById("nope")).thenReturn(Optional.empty());

        ColeccionOutputDTO out = service.retrieveColeccion("nope");

        assertThat(out).isNull();
        verify(coleccionesRepository).findById("nope");
    }


    @Test
    void retrieveHechosColeccion_conCriterios() {
        var c = coleccion("c1", "T1", "D1", List.of("Fuente-A"));
        when(coleccionesRepository.findById("c1")).thenReturn(Optional.of(c));

        Map<String,String> qp = Map.of("tipo", "porCategoria", "categoriaDeseada", "Incendio");

        var criterio = new CriterioRequest();
        criterio.setTipo("porCategoria");
        criterio.setParams(Map.of("categoriaDeseada", "Incendio"));

        var criterios = List.of(criterio);
        var hechosFiltrados = List.of(new Hecho(), new Hecho());
        var hechosDTO = List.of(
                new HechoOutputDTO(), new HechoOutputDTO()
        );

        try (MockedStatic<StringAObjetos> st = mockStatic(StringAObjetos.class)) {
            st.when(() -> StringAObjetos.convertirQueryParamsACriterios(qp))
                    .thenReturn(criterios);

            st.when(() -> StringAObjetos.criterioFactory(any(CriterioRequest.class)))
                    .thenReturn(mock(CondicionDeFiltrado.class));

            when(filtradorService.filtrarHechosDataBase(anyList())).thenReturn(hechosFiltrados);
            when(hechoService.hechoADTOOuts(hechosFiltrados)).thenReturn(hechosDTO);

            var out = service.retrieveHechosColeccion("c1", qp);

            assertThat(out).hasSize(2);
            verify(coleccionesRepository).findById("c1");
            verify(hechoService).hechoADTOOuts(hechosFiltrados);
        }
    }

    @Test
    void retrieveHechosColeccion_sinCriterios() {
        var c = coleccion("c2", "T2", "D2", List.of("Fuente-X"));
        when(coleccionesRepository.findById("c2")).thenReturn(Optional.of(c));

        Map<String,String> qp = Collections.emptyMap();

        var hechosFiltrados = List.of(new Hecho());
        var hechosDTO = List.of(new HechoOutputDTO());

        try (MockedStatic<StringAObjetos> st = mockStatic(StringAObjetos.class)) {
            st.when(() -> StringAObjetos.convertirQueryParamsACriterios(qp))
                    .thenReturn(List.of());

            when(filtradorService.filtrarHechosDataBase(anyList())).thenReturn(hechosFiltrados);
            when(hechoService.hechoADTOOuts(hechosFiltrados)).thenReturn(hechosDTO);

            var out = service.retrieveHechosColeccion("c2", qp);

            assertThat(out).hasSize(1);
            verify(coleccionesRepository).findById("c2");
            verify(filtradorService).filtrarHechosDataBase(anyList());
        }
    }

    @Test
    void retrieveHechosColeccion_coleccionInexistente() {
        when(coleccionesRepository.findById("x")).thenReturn(Optional.empty());
        var out = service.retrieveHechosColeccion("x", Map.of("algo", "1"));
        assertThat(out).isNull();
    }


    @Test
    void retrieveColeccionModoNavegacion_valido() {
        Coleccion coleccionMock = mock(Coleccion.class);
        when(coleccionesRepository.findById("c3")).thenReturn(Optional.of(coleccionMock));

        Map<String,String> qp = Map.of("modo", "curada");
        List<Hecho> hechos = List.of(new Hecho(), new Hecho());
        List<HechoOutputDTO> hechosDTO = List.of(new HechoOutputDTO(), new HechoOutputDTO());

        try (MockedStatic<StringAObjetos> st = mockStatic(StringAObjetos.class)) {
            ModoNavegacion modoMock = mock(ModoNavegacion.class);
            st.when(() -> StringAObjetos.modoNavegacionFactory("curada")).thenReturn(modoMock);

            when(coleccionMock.obtenerHechosPorModo(modoMock)).thenReturn(hechos);
            when(hechoService.hechoADTOOuts(hechos)).thenReturn(hechosDTO);

            var out = service.retrieveColeccionModoNavegacion("c3", qp);

            assertThat(out).hasSize(2);
            verify(coleccionMock).obtenerHechosPorModo(modoMock);
            verify(hechoService).hechoADTOOuts(hechos);
        }
    }

    @Test
    void retrieveColeccionModoNavegacion_sinModo() {
        var c = coleccion("c4", "T4", "D4", null);
        when(coleccionesRepository.findById("c4")).thenReturn(Optional.of(c));

        var out = service.retrieveColeccionModoNavegacion("c4", Map.of());
        assertThat(out).isNull();
    }

    @Test
    void retrieveColeccionModoNavegacion_invalido() {
        var c = coleccion("c5", "T5", "D5", null);
        when(coleccionesRepository.findById("c5")).thenReturn(Optional.of(c));

        try (MockedStatic<StringAObjetos> st = mockStatic(StringAObjetos.class)) {
            st.when(() -> StringAObjetos.modoNavegacionFactory("raro"))
                    .thenThrow(new IllegalArgumentException("modo inválido"));
            var out = service.retrieveColeccionModoNavegacion("c5", Map.of("modo", "raro"));
            assertThat(out).isNull();
        }
    }

    @Test
    void retrieveColeccionModoNavegacion_coleccionInexistente() {
        when(coleccionesRepository.findById("cx")).thenReturn(Optional.empty());
        var out = service.retrieveColeccionModoNavegacion("cx", Map.of("modo", "curada"));
        assertThat(out).isNull();
    }
}
