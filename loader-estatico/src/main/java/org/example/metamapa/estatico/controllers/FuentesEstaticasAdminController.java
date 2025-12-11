package org.example.metamapa.estatico.controllers;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.estatico.models.dtos.FuenteEstaticaDTO;
import org.example.metamapa.estatico.models.dtos.FuenteCsvUrlRequest;
import org.example.metamapa.estatico.service.IFuentesEstaticasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/fuenteEstatica/admin/fuentes")
@RequiredArgsConstructor
public class FuentesEstaticasAdminController {

    private final IFuentesEstaticasService fuentesService;

    // Registrar una fuente estática subiendo el CSV desde la PC
    @PostMapping("/csv")
    public ResponseEntity<FuenteEstaticaDTO> registrarFuenteCsv(
            @RequestParam String nombreFuente,
            @RequestParam("archivo") MultipartFile archivoCsv) {

        FuenteEstaticaDTO fuente = fuentesService.registrarFuenteDesdeCsv(nombreFuente, archivoCsv);
        return ResponseEntity.status(HttpStatus.CREATED).body(fuente);
    }

    // Registrar una fuente estática a partir de una URL (CSV público, Drive, etc.)
    @PostMapping("/csv-url")
    public ResponseEntity<FuenteEstaticaDTO> registrarFuenteCsvPorUrl(
            @RequestBody FuenteCsvUrlRequest request) {

        FuenteEstaticaDTO fuente = fuentesService.registrarFuenteDesdeUrl(
                request.getNombreFuente(),
                request.getUrlCsv()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(fuente);
    }

    // Listar todas las fuentes estáticas registradas (para mostrar en la UI)
    @GetMapping
    public ResponseEntity<List<FuenteEstaticaDTO>> listarFuentes() {
        List<FuenteEstaticaDTO> fuentes = fuentesService.listarFuentes();
        if (fuentes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(fuentes);
    }
}
