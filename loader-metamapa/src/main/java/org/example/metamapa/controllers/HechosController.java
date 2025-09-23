package org.example.metamapa.controllers;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.service.ICargaMetamapaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/fuenteMetamapa")
@RequiredArgsConstructor
public class HechosController {

    private final ICargaMetamapaService cargaMetamapaService;

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoDTO>> obtenerHechos() {
        return cargaMetamapaService.obtenerHechos();
    }
}
