package org.example.metamapa.estatico.adapters.implementaciones;

import org.example.metamapa.estatico.adapters.IAdapterFileServer;
import org.example.metamapa.estatico.models.dtos.ArchivoCsv;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "fileserver.tipo", havingValue = "local")
public class AdapterFileServerLocal implements IAdapterFileServer {

    @Value("${fileserver.basePath}")
    private String basePath;

    @Override
    public List<ArchivoCsv> obtenerArchivosDisponibles() {
        File carpeta = new File(basePath);
        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".csv"));

        if (archivos == null) return List.of();

        return Arrays.stream(archivos)
                .map(file -> {
                    try {
                        return new ArchivoCsv(file.getName(), new FileInputStream(file));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
    public List<HechoCrudo> leerArchivoDesdeBytes(byte[] contenido) throws IOException {

        return CsvParserUtil.parsearDesdeBytes(contenido);
    }
}

