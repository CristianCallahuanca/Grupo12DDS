package org.example.metamapa.estatico.service.implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.models.dtos.FuenteEstaticaDTO;
import org.example.metamapa.estatico.models.entidades.FuenteEstatica;
import org.example.metamapa.estatico.models.repositorios.IFuenteEstaticaRepositorio;
import org.example.metamapa.estatico.service.IFuentesEstaticasService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuentesEstaticasService implements IFuentesEstaticasService {

    private final IFuenteEstaticaRepositorio fuenteRepo;

    @Value("${fileserver.basePath}")
    private String baseDir; // o podemos agregar esta base dentro del proyecto "./data/csv"

    @Override
    public FuenteEstaticaDTO registrarFuenteDesdeCsv(String nombreFuente, MultipartFile archivoCsv) {
        // 1) Creamos la entidad sin ruta aún
        FuenteEstatica fuente = FuenteEstatica.builder()
                .nombreFuente(nombreFuente)
                .nombreArchivoOriginal(archivoCsv.getOriginalFilename())
                .activa(true)
                .pendienteProcesar(true)
                .fechaRegistro(LocalDateTime.now())
                .build();

        fuente = fuenteRepo.save(fuente); // obtengo el id

        // 2) Construyo una ruta de archivo estable a partir del id
        Path dir = Path.of(baseDir);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            log.error("No se pudo crear el directorio de CSVs {}: {}", baseDir, e.getMessage());
            throw new RuntimeException("Error inicializando directorio de CSV", e);
        }

        String nombreFs = "fuente_" + fuente.getId() + ".csv";
        Path ruta = dir.resolve(nombreFs);

        // 3) Guardo físicamente el archivo
        try {
            archivoCsv.transferTo(ruta.toFile());
        } catch (IOException e) {
            log.error("Error guardando archivo CSV {} para la fuente {}: {}",
                    ruta, nombreFuente, e.getMessage());
            throw new RuntimeException("Error guardando archivo CSV", e);
        }

        // 4) Actualizo la ruta en la entidad
        fuente.setRutaArchivoCsv(ruta.toString());
        fuenteRepo.save(fuente);

        return mapearADTO(fuente);
    }

    @Override
    public FuenteEstaticaDTO actualizarFuenteCsv(Long fuenteId, MultipartFile archivoCsv) {
        FuenteEstatica fuente = fuenteRepo.findById(fuenteId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró la fuente estática con id " + fuenteId));

        Path ruta = Path.of(fuente.getRutaArchivoCsv());
        try {
            // Sobrescribo el archivo anterior con el nuevo contenido
            archivoCsv.transferTo(ruta.toFile());
        } catch (IOException e) {
            log.error("Error actualizando archivo CSV {} para la fuente {}: {}",
                    ruta, fuente.getNombreFuente(), e.getMessage());
            throw new RuntimeException("Error actualizando archivo CSV", e);
        }

        // Actualizo metadatos visibles
        fuente.setNombreArchivoOriginal(archivoCsv.getOriginalFilename());
        // MUY IMPORTANTE: marco como pendiente reprocesar
        fuente.setPendienteProcesar(true);

        fuenteRepo.save(fuente);

        return mapearADTO(fuente);
    }

    @Override
    public List<FuenteEstaticaDTO> listarFuentes() {
        return fuenteRepo.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    private FuenteEstaticaDTO mapearADTO(FuenteEstatica f) {
        return new FuenteEstaticaDTO(
                f.getId(),
                f.getNombreFuente(),
                f.getNombreArchivoOriginal(),
                f.getPendienteProcesar(),
                f.getActiva(),
                f.getFechaUltimoProcesamiento()
        );
    }
}
