package org.example.metamapa.estatico.controllers;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.estatico.models.dtos.FuenteEstaticaDTO;
import org.example.metamapa.estatico.service.IFuentesEstaticasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/fuenteEstatica/admin/fuentes")
@RequiredArgsConstructor
public class FuentesEstaticasAdminController {

    private final IFuentesEstaticasService fuentesService;

    @PostMapping("/csv")
    public ResponseEntity<FuenteEstaticaDTO> registrarFuenteCsv(
            @RequestParam String nombreFuente,
            @RequestParam("archivo") MultipartFile archivoCsv) throws IOException {

        System.out.println("euuuuuuuu subieron un CSV XD");

        FuenteEstaticaDTO fuente = fuentesService.registrarFuenteDesdeCsv(nombreFuente, archivoCsv);
        return ResponseEntity.status(HttpStatus.CREATED).body(fuente);
    }


    @GetMapping
    public ResponseEntity<List<FuenteEstaticaDTO>> listarFuentes() {
        List<FuenteEstaticaDTO> fuentes = fuentesService.listarFuentes();
        if (fuentes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(fuentes);
    }
}

