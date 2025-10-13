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
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "fileserver.tipo", havingValue = "remoto")
@Slf4j
public class AdapterFileServerRemoto implements IAdapterFileServer {

    @Value("${fileserver.baseUrl}")
    private String baseUrl;

    private final RestClient client = RestClient.create();
    private final ParserFactory parserFactory;

    public AdapterFileServerRemoto(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    @Override
    public List<ArchivoCsv> obtenerArchivosDisponibles() {
        String[] nombres = client.get()
                .uri(baseUrl + "/archivos")
                .retrieve()
                .body(String[].class);

        if (nombres == null || nombres.length == 0) {
            log.warn("No se encontraron archivos remotos en {}", baseUrl);
            return List.of();
        }

        log.info("Archivos detectados en remoto: {}", Arrays.toString(nombres));

        return Arrays.stream(nombres)
                .map(nombre -> {
                    try {
                        return new ArchivoCsv(nombre, descargarComoInputStream(nombre));
                    } catch (IOException e) {
                        throw new RuntimeException("Error descargando archivo remoto: " + nombre, e);
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
    public List<HechoCrudo> leerArchivoDesdeBytes(String nombreArchivo, byte[] contenido) throws IOException {
        IParserDeArchivo parser = parserFactory.obtenerParser(nombreArchivo);
        log.debug("Usando parser {} para archivo remoto {}", parser.getClass().getSimpleName(), nombreArchivo);
        return parser.parse(nombreArchivo, contenido);
    }
}
