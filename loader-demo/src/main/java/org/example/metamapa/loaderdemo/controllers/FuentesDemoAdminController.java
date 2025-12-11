package org.example.metamapa.loaderdemo.controllers;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.models.dto.FuenteDemoDTO;
import org.example.metamapa.loaderdemo.models.dto.FuenteDemoRequest;
import org.example.metamapa.loaderdemo.service.IFuentesDemoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuenteDemo/admin/fuentes")
@RequiredArgsConstructor
public class FuentesDemoAdminController {

    private final IFuentesDemoService fuentesDemoService;

    @PostMapping
    public ResponseEntity<FuenteDemoDTO> registrarFuenteDemo(
            @RequestBody FuenteDemoRequest request) {

        FuenteDemoDTO fuente = fuentesDemoService.registrarFuenteDemo(
                request.getNombreFuente(),
                request.getUrl()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(fuente);
    }

    @GetMapping
    public ResponseEntity<List<FuenteDemoDTO>> listarFuentesDemo() {
        List<FuenteDemoDTO> fuentes = fuentesDemoService.listarFuentesDemo();
        if (fuentes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(fuentes);
    }
}
