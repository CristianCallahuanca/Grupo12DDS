package org.example.metamapa.loaderdemo.controllers;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.models.dto.HechoDTO;
import org.example.metamapa.loaderdemo.service.IHechosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuenteDemo")
@RequiredArgsConstructor
public class HechosController {

    private final IHechosService hechosService;

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoDTO>> obtenerHechos() {
        List<HechoDTO> hechos = hechosService.listarHechos();

        if (hechos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(hechos); // 200
    }
}
