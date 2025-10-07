package org.example.metamapa.estatico.controllers;

import org.example.metamapa.estatico.models.dtos.HechoDTO;
import org.example.metamapa.estatico.service.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuenteEstatica")
public class HechosController {

    private final IHechosService hechosService;

    @Autowired
    public HechosController(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoDTO>> obtenerHechos() {
        List<HechoDTO> hechos = hechosService.obtenerHechos();
        return ResponseEntity.ok(hechos);
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Loader Estático disponible");
    }
}
