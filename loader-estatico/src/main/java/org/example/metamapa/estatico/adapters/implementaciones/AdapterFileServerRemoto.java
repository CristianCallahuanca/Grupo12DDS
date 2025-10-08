package org.example.metamapa.estatico.adapters.implementaciones;

import org.example.metamapa.estatico.adapters.IAdapterFileServer;
import org.example.metamapa.estatico.models.dtos.ArchivoCsv;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "fileserver.tipo", havingValue = "remoto")
public class AdapterFileServerRemoto implements IAdapterFileServer {

    @Value("${fileserver.baseUrl}")
    private String baseUrl;

    private final RestClient client = RestClient.create();

    @Override
    public List<ArchivoCsv> obtenerArchivosDisponibles() {
        String[] nombres = client.get()
                .uri(baseUrl + "/archivos")
                .retrieve()
                .body(String[].class);

        return Arrays.stream(nombres)
                .map(nombre -> {
                    try {
                        return new ArchivoCsv(nombre, descargarComoInputStream(nombre));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private InputStream descargarComoInputStream(String nombre) throws IOException {
        return client.get()
                .uri(baseUrl + "/archivos/" + nombre)
                .retrieve()
                .body(InputStream.class);
    }

    @Override
    public byte[] descargarArchivo(String nombreArchivo) throws IOException {
        return client.get()
                .uri(baseUrl + "/archivos/" + nombreArchivo)
                .retrieve()
                .body(byte[].class);
    }

    @Override
    public List<HechoCrudo> leerArchivoDesdeBytes(byte[] contenido) throws IOException {
        return CsvParserUtil.parsearDesdeBytes(contenido);
    }

}
