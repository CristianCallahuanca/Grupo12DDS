package org.example.metamapa.estatico.service.implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.models.dtos.FuenteEstaticaDTO;
import org.example.metamapa.estatico.models.entidades.FuenteEstatica;
import org.example.metamapa.estatico.models.repositorios.IFuenteEstaticaRepositorio;
import org.example.metamapa.estatico.service.IFuentesEstaticasService;
import org.example.metamapa.estatico.service.IProcesadorCsvService;
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
    private final IProcesadorCsvService procesadorCsvService;

    @Value("${fileserver.basePath}")
    private String baseDir; // o podemos agregar esta base dentro del proyecto "./data/csv"

    @Override
    public FuenteEstaticaDTO registrarFuenteDesdeCsv(
            String nombreFuente,
            MultipartFile archivoCsv
    ) throws IOException {

        // 1️⃣ Persistís la fuente
        FuenteEstatica fuente = FuenteEstatica.builder()
                .nombreFuente(nombreFuente)
                .nombreArchivoOriginal(archivoCsv.getOriginalFilename())
                .activa(true)
                .pendienteProcesar(true)
                .fechaRegistro(LocalDateTime.now())
                .build();

        fuente = fuenteRepo.save(fuente);

        // 2️⃣ Procesás directamente desde el archivo recibido
        procesadorCsvService.procesarFuenteDesdeBytes(
                fuente,
                archivoCsv.getBytes(),
                archivoCsv.getOriginalFilename()
        );

        return mapearADTO(fuente);
    }

    /*
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
    }*/

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
