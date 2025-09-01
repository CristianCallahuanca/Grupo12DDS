package org.example.metamapa.publica.controllers;


import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.publica.service.IColeccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/colecciones")
public class ColeccionController {

    private final IColeccionService coleccionService;

    public ColeccionController(IColeccionService navegacionService) {
        this.coleccionService = navegacionService;
    }

    @GetMapping("/{id}/hechos")
    public ResponseEntity<List<HechoOutputDTO>> obtenerHechosDeColeccion(@PathVariable String id) {
        List<HechoOutputDTO> hechos = coleccionService.obtenerHechosDeColeccion(id);
        return ResponseEntity.ok(hechos);
    }
}
