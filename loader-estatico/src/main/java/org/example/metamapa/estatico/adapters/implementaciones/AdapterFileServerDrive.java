package org.example.metamapa.estatico.adapters.implementaciones;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
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
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "fileserver.tipo", havingValue = "drive")
@Slf4j
public class AdapterFileServerDrive implements IAdapterFileServer {

    private final ParserFactory parserFactory;
    private final Drive driveService;

    @Value("${fileserver.drive.folderId}")
    private String folderId;

    public AdapterFileServerDrive(ParserFactory parserFactory,
                                  @Value("${fileserver.drive.credentialsPath}") String credentialsPath) throws Exception {
        this.parserFactory = parserFactory;

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialsPath))
                .createScoped(List.of("https://www.googleapis.com/auth/drive.readonly"));

        driveService = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("MetaMapa Loader Drive").build();
    }

    @Override
    public List<ArchivoCsv> obtenerArchivosDisponibles() {
        try {
            FileList result = driveService.files().list()
                    .setQ("'" + folderId + "' in parents and trashed=false")
                    .setFields("files(id, name, mimeType)")
                    .execute();

            List<File> archivos = result.getFiles().stream()
                    .filter(f -> f.getName().endsWith(".csv") || f.getName().endsWith(".xlsx"))
                    .collect(Collectors.toList());

            if (archivos.isEmpty()) {
                log.warn("No se encontraron archivos en la carpeta de Drive {}", folderId);
                return List.of();
            }

            log.info("Archivos detectados en Drive: {}", archivos.stream().map(File::getName).toList());

            return archivos.stream()
                    .map(f -> {
                        try {
                            byte[] contenido = descargarArchivo(f.getId());
                            return new ArchivoCsv(f.getName(), new ByteArrayInputStream(contenido));
                        } catch (IOException e) {
                            log.error("Error descargando archivo {} desde Drive: {}", f.getName(), e.getMessage());
                            return null; // o podrías usar Optional.empty() si preferís filtrarlo después
                        }
                    })
                    .filter(a -> a != null)
                    .toList();


        } catch (Exception e) {
            log.error("Error accediendo a la carpeta de Drive: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public byte[] descargarArchivo(String fileId) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        driveService.files().get(fileId).executeMediaAndDownloadTo(output);
        return output.toByteArray();
    }

    @Override
    public List<HechoCrudo> leerArchivoDesdeBytes(String nombreArchivo, byte[] contenido) throws IOException {
        IParserDeArchivo parser = parserFactory.obtenerParser(nombreArchivo);
        log.debug("Usando parser {} para archivo de Drive {}", parser.getClass().getSimpleName(), nombreArchivo);
        return parser.parse(nombreArchivo, contenido);
    }
}
