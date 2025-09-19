package org.example.metamapa.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.service.ICargaProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hechos")
@Slf4j
public class HechosProxyController {

    @Autowired
    private ICargaProxyService cargaProxyService;

    @GetMapping
    public ResponseEntity<List<HechoDTO>> cargarHechosDesdeFuentes() {
        log.info("Peticion para cargar Hechos desdes Fuentes");
        List<HechoDTO> hechos = cargaProxyService.cargarHechos();
        return ResponseEntity.ok(hechos);
    }
}
