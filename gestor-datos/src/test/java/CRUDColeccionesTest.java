
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.Servicios.Implementaciones.ColeccionesService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.repositorios.IColeccionesRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class CRUDColeccionesTest {

    @Mock private IColeccionesRepository coleccionesRepository;
    @Mock private IHechoService hechoService;

    @InjectMocks
    private ColeccionesService service;


    private ColeccionInputDTO dtoBasico() {
        var dto = new ColeccionInputDTO();
        dto.setTitulo("Colección Prueba");
        dto.setDescripcion("Desc de prueba");
        dto.setAlgoritmoConsenso("MAYORIA_SIMPLE");
        dto.setOrigenesReales(List.of("Fuente-CSV-Hogar"));
        dto.setCriterios(Collections.emptyList());
        return dto;
    }

    private Coleccion coleccionPersistida(String handle) {
        Coleccion c = new Coleccion(
                handle, null, new ArrayList<>(),
                "Colección Prueba", "Desc de prueba",
                new ArrayList<>(), null
        );
        c.setOrigenesReales(new ArrayList<>(List.of("Fuente-CSV-Hogar")));
        return c;
    }

    // =========================================================
    //                  CREAR COLECCIÓN  (C)
    // =========================================================

    @Test
    void crearColeccion() {
        var dto = dtoBasico();

        try (MockedStatic<StringAObjetos> mockedStatic = mockStatic(StringAObjetos.class)) {
            var algoritmoMock = mock(AlgoritmoConsenso.class);
            when(algoritmoMock.getNombre()).thenReturn("MAYORIA_SIMPLE");

            mockedStatic.when(() -> StringAObjetos.algoritmoConsensoFactory("MAYORIA_SIMPLE"))
                    .thenReturn(algoritmoMock);

            when(hechoService.contarTodos()).thenReturn(2L);
            when(hechoService.filtrarHechos(anyList())).thenReturn(List.of(new Hecho(), new Hecho()));

            doAnswer(invocation -> invocation.getArgument(0))
                    .when(coleccionesRepository).save(any(Coleccion.class));

            ColeccionOutputDTO out = service.crearColeccion(dto);

            assertThat(out).isNotNull();
            assertThat(out.getTitulo()).isEqualTo("Colección Prueba");
            assertThat(out.getDescripcion()).isEqualTo("Desc de prueba");
            assertThat(out.getOrigenesReales()).containsExactly("Fuente-CSV-Hogar");
            assertThat(out.getAlgoritmo()).isEqualTo("MAYORIA_SIMPLE");

            verify(coleccionesRepository, times(1)).save(any(Coleccion.class));
            verify(hechoService, times(1)).filtrarHechos(anyList());
        }
    }

    @Test
    void listarColecciones() {
        Coleccion c1 = coleccionPersistida("handle-1");
        c1.setTitulo("C1");
        Coleccion c2 = coleccionPersistida("handle-2");
        c2.setTitulo("C2");

        when(coleccionesRepository.findAll()).thenReturn(List.of(c1, c2));

        var lista = service.listarColecciones();

        assertThat(lista).hasSize(2);
        assertThat(lista).extracting("titulo").containsExactly("C1", "C2");
        verify(coleccionesRepository).findAll();
    }

    @Test
    void actualizarColeccion_existente() {
        Coleccion existente = coleccionPersistida("handle-123");
        when(coleccionesRepository.findById("handle-123")).thenReturn(Optional.of(existente));

        Map<String, String> cambios = Map.of(
                "titulo", "Nuevo Título",
                "descripcion", "Nueva Descripción"
        );

        boolean ok = service.actualizarColeccion("handle-123", cambios);

        assertThat(ok).isTrue();
        assertThat(existente.getTitulo()).isEqualTo("Nuevo Título");
        assertThat(existente.getDescripcion()).isEqualTo("Nueva Descripción");
        verify(coleccionesRepository).save(existente);
    }

    @Test
    void actualizarColeccion_inexistente() {
        when(coleccionesRepository.findById("no-existe")).thenReturn(Optional.empty());
        boolean ok = service.actualizarColeccion("no-existe", Map.of("titulo", "X"));
        assertThat(ok).isFalse();
        verify(coleccionesRepository, never()).save(any());
    }

    @Test
    void eliminarColeccion_existente() {
        Coleccion existente = coleccionPersistida("handle-xyz");
        when(coleccionesRepository.findById("handle-xyz")).thenReturn(Optional.of(existente));

        boolean ok = service.eliminarColeccion("handle-xyz");

        assertThat(ok).isTrue();
        verify(coleccionesRepository).delete(existente);
    }

    @Test
    void eliminarColeccion_inexistente() {
        when(coleccionesRepository.findById("no-existe")).thenReturn(Optional.empty());

        boolean ok = service.eliminarColeccion("no-existe");

        assertThat(ok).isFalse();
        verify(coleccionesRepository, never()).delete(any());
    }


    //cambiar algoritmo de consenso
    @Test
    void updateAlgoritmo_existente() {
        Coleccion existente = coleccionPersistida("handle-abc");
        when(coleccionesRepository.findById("handle-abc")).thenReturn(Optional.of(existente));

        try (MockedStatic<StringAObjetos> mockedStatic = mockStatic(StringAObjetos.class)) {
            var algoritmoMock = mock(AlgoritmoConsenso.class);
            when(algoritmoMock.getNombre()).thenReturn("MAYORIA_ABSOLUTA");
            mockedStatic.when(() -> StringAObjetos.algoritmoConsensoFactory("MAYORIA_ABSOLUTA"))
                    .thenReturn(algoritmoMock);

            ColeccionOutputDTO out = service.updateAlgoritmo("handle-abc", "MAYORIA_ABSOLUTA");

            assertThat(out).isNotNull();
            assertThat(out.getAlgoritmo()).isEqualTo("MAYORIA_ABSOLUTA");
            verify(coleccionesRepository).save(existente);
        }
    }

    @Test
    void updateAlgoritmo_inexistente() {
        when(coleccionesRepository.findById("no-existe")).thenReturn(Optional.empty());
        var out = service.updateAlgoritmo("no-existe", "MAYORIA_SIMPLE");
        assertThat(out).isNull();
        verify(coleccionesRepository, never()).save(any());
    }
}
