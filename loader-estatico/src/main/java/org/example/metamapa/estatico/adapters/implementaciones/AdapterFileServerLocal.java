package org.example.metamapa.estatico.adapters.implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.adapters.IAdapterFileServer;
import org.example.metamapa.estatico.adapters.parsers.IParserDeArchivo;
import org.example.metamapa.estatico.adapters.parsers.ParserFactory;
import org.example.metamapa.estatico.models.dtos.ArchivoCsv;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "fileserver.tipo", havingValue = "local")
@Slf4j
public class AdapterFileServerLocal implements IAdapterFileServer {

    @Value("${fileserver.basePath}")
    private String basePath;

    private final ParserFactory parserFactory;

    public AdapterFileServerLocal(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    @Override
    public List<ArchivoCsv> obtenerArchivosDisponibles() {
        File carpeta = new File(basePath);
        File[] archivos = carpeta.listFiles((dir, name) ->
                name.endsWith(".csv") || name.endsWith(".xls") || name.endsWith(".xlsx"));

        if (archivos == null || archivos.length == 0) {
            log.warn("No se encontraron archivos válidos en {}", basePath);
            return List.of();
        }

        log.info("Archivos detectados localmente: {}",
                Arrays.stream(archivos).map(File::getName).collect(Collectors.joining(", ")));

        return Arrays.stream(archivos)
                .map(file -> {
                    try {
                        return new ArchivoCsv(file.getName(), new FileInputStream(file));
                    } catch (IOException e) {
                        throw new RuntimeException("Error al abrir archivo: " + file.getName(), e);
                    }
                })
                .toList();
    }

    @Override
    public byte[] descargarArchivo(String nombreArchivo) throws IOException {
        File archivo = new File(basePath, nombreArchivo);
        return Files.readAllBytes(archivo.toPath());
    }

    @Override
    public List<HechoCrudo> leerArchivoDesdeBytes(String nombreArchivo, byte[] contenido) throws IOException {
        IParserDeArchivo parser = parserFactory.obtenerParser(nombreArchivo);
        log.debug("Usando parser {} para archivo {}", parser.getClass().getSimpleName(), nombreArchivo);
        return parser.parse(nombreArchivo, contenido);
    }
}
