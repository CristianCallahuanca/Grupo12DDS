package org.example.metamapa.controllers;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.service.ICargaMetamapaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuenteMetamapa")
@RequiredArgsConstructor
public class HechosController {

    private final ICargaMetamapaService cargaMetamapaService;

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoDTO>> obtenerHechos() {
        List<HechoDTO> hechos = cargaMetamapaService.obtenerHechos();

        if (hechos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(hechos); // 200
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Loader Metamapa disponible");
    }
}
