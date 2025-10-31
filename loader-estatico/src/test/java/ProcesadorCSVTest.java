import org.example.metamapa.estatico.adapters.IAdapterFileServer;
import org.example.metamapa.estatico.models.dtos.ArchivoCsv;
import org.example.metamapa.estatico.models.entidades.CsvProcesado;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.example.metamapa.estatico.models.repositorios.IRepositorioCSVProcesado;
import org.example.metamapa.estatico.models.repositorios.IRepositorioHechos;
import org.example.metamapa.estatico.service.implementaciones.ProcesadorCsvService;
import org.example.metamapa.estatico.service.implementaciones.HashUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.argThat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ProcesadorCSVTest {

    @Mock private IAdapterFileServer adapter;
    @Mock private IRepositorioCSVProcesado repositorioCSV;
    @Mock private IRepositorioHechos repositorioHechos;

    @InjectMocks
    private ProcesadorCsvService service;

    private final String LOADER_ID = "ESTATICO-001";
    private final String ARCHIVO_NOMBRE = "data.csv";
    private final byte[] ARCHIVO_CONTENIDO = "titulo,desc\nA,B".getBytes();
    private final String NUEVO_HASH = "nuevo_hash_123";
    private final String VIEJO_HASH = "viejo_hash_321";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "loaderId", LOADER_ID);
    }

    private ArchivoCsv crearArchivoMock(String nombre, byte[] contenido) throws IOException {
        ArchivoCsv mockArchivo = mock(ArchivoCsv.class);
        when(mockArchivo.getNombre()).thenReturn(nombre);
        when(mockArchivo.leerComoBytes()).thenReturn(contenido);
        return mockArchivo;
    }

// tests para procesarArchivosCSV()
    @Test
    void procesarArchivosCsv_nuevoArchivo_guardaHechosYRegistro() throws IOException {

        HechoCrudo hechoMock = mock(HechoCrudo.class);
        ArchivoCsv archivoMock = crearArchivoMock(ARCHIVO_NOMBRE, ARCHIVO_CONTENIDO);

        when(adapter.obtenerArchivosDisponibles()).thenReturn(List.of(archivoMock));

        // Simulo el Hash
        try (MockedStatic<HashUtil> hashMock = mockStatic(HashUtil.class)) {
            hashMock.when(() -> HashUtil.calcularSHA256(ARCHIVO_CONTENIDO)).thenReturn(NUEVO_HASH);

            // Simular que el archivo no fue procesado antes (debeProcesarse == true)
            when(repositorioCSV.existsById_LoaderIdAndId_NombreArchivo(LOADER_ID, ARCHIVO_NOMBRE)).thenReturn(false);

            when(adapter.leerArchivoDesdeBytes(ARCHIVO_NOMBRE, ARCHIVO_CONTENIDO)).thenReturn(Collections.nCopies(10, hechoMock));

            service.procesarArchivosCsv();

            verify(repositorioHechos, times(1)).saveAll(
                    argThat((List<HechoCrudo> list) -> list.size() == 10)
            );
            verify(repositorioCSV, times(1)).save(any(CsvProcesado.class));
            verify(hechoMock, times(10)).setLoaderId(LOADER_ID);
        }
    }

    @Test
    void procesarArchivosCsv_archivoSinCambios_omiteProcesado() throws IOException {

        ArchivoCsv archivoMock = crearArchivoMock(ARCHIVO_NOMBRE, ARCHIVO_CONTENIDO);

        when(adapter.obtenerArchivosDisponibles()).thenReturn(List.of(archivoMock));

        try (MockedStatic<HashUtil> hashMock = mockStatic(HashUtil.class)) {
            hashMock.when(() -> HashUtil.calcularSHA256(ARCHIVO_CONTENIDO)).thenReturn(VIEJO_HASH);

            when(repositorioCSV.existsById_LoaderIdAndId_NombreArchivo(LOADER_ID, ARCHIVO_NOMBRE)).thenReturn(true);
            // Simula que el hash no cambió (debeProcesarse == false)
            when(repositorioCSV.obtenerHashPorNombre(LOADER_ID, ARCHIVO_NOMBRE)).thenReturn(VIEJO_HASH);

            service.procesarArchivosCsv();

            // Verifica: no tiene q leer el contenido (el adapter) ni guardar nada
            verify(adapter, never()).leerArchivoDesdeBytes(any(), any());
            verify(repositorioHechos, never()).saveAll(any());
            verify(repositorioCSV, never()).save(any(CsvProcesado.class));
        }
    }

    @Test
    void procesarArchivosCsv_archivoConCambios_actualizaYGuarda() throws IOException {

        HechoCrudo hechoMock = mock(HechoCrudo.class);
        ArchivoCsv archivoMock = crearArchivoMock(ARCHIVO_NOMBRE, ARCHIVO_CONTENIDO);

        when(adapter.obtenerArchivosDisponibles()).thenReturn(List.of(archivoMock));

        try (MockedStatic<HashUtil> hashMock = mockStatic(HashUtil.class)) {
            hashMock.when(() -> HashUtil.calcularSHA256(ARCHIVO_CONTENIDO)).thenReturn(NUEVO_HASH);

            when(repositorioCSV.existsById_LoaderIdAndId_NombreArchivo(LOADER_ID, ARCHIVO_NOMBRE)).thenReturn(true);
            // Simula que el hash sí cambió (debeProcesarse == true)
            when(repositorioCSV.obtenerHashPorNombre(LOADER_ID, ARCHIVO_NOMBRE)).thenReturn(VIEJO_HASH);

            // Simula que se leen 5 hechos válidos del CSV
            when(adapter.leerArchivoDesdeBytes(ARCHIVO_NOMBRE, ARCHIVO_CONTENIDO)).thenReturn(Collections.nCopies(5, hechoMock));

            service.procesarArchivosCsv();

            verify(repositorioHechos, times(1)).saveAll(argThat((List<HechoCrudo>list) -> list.size() == 5));
            verify(repositorioCSV, times(1)).save(any(CsvProcesado.class));
        }
    }

    @Test
    void procesarArchivosCsv_adapterRetornaVacio_noHaceNada() {

        when(adapter.obtenerArchivosDisponibles()).thenReturn(Collections.emptyList());

        service.procesarArchivosCsv();

        verify(adapter, times(1)).obtenerArchivosDisponibles();
        verifyNoInteractions(repositorioCSV, repositorioHechos); // No tiene q interactuar con repositorios porque no hay archivos csv
    }

    @Test
    void procesarArchivosCsv_archivoSinHechos_omiteGuardado() throws IOException {

        ArchivoCsv archivoMock = crearArchivoMock(ARCHIVO_NOMBRE, ARCHIVO_CONTENIDO);

        when(adapter.obtenerArchivosDisponibles()).thenReturn(List.of(archivoMock));

        try (MockedStatic<HashUtil> hashMock = mockStatic(HashUtil.class)) {
            hashMock.when(() -> HashUtil.calcularSHA256(ARCHIVO_CONTENIDO)).thenReturn(NUEVO_HASH);

            when(repositorioCSV.existsById_LoaderIdAndId_NombreArchivo(LOADER_ID, ARCHIVO_NOMBRE)).thenReturn(false);

            // Simula que el CSV no genera hechos válidos (lista vacía)
            when(adapter.leerArchivoDesdeBytes(ARCHIVO_NOMBRE, ARCHIVO_CONTENIDO)).thenReturn(Collections.emptyList());

            service.procesarArchivosCsv();

            // No tiene q guardar hechos ni el registro de CSV
            verify(repositorioHechos, never()).saveAll(any());
            verify(repositorioCSV, never()).save(any(CsvProcesado.class));
        }
    }
}

