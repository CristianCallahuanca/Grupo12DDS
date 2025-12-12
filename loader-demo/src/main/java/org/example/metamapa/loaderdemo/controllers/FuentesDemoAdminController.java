package org.example.metamapa.loaderdemo.controllers;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.infraestructura.externos.Conexion;
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
    private final Conexion conexion;  // Para autenticar las fuentes

    @PostMapping
    public ResponseEntity<FuenteDemoDTO> registrarFuenteDemo(
            @RequestBody FuenteDemoRequest request) {

        try {
            if (request.getEmail() != null && !request.getEmail().isBlank()
                    && request.getPassword() != null && !request.getPassword().isBlank()) {
                boolean autenticado = conexion.validarAutenticacion(request.getEmail(), request.getPassword(), request.getUrl());

                if (!autenticado) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(null);
                }
            }

            FuenteDemoDTO fuente = fuentesDemoService.registrarFuenteDemo(
                    request.getNombreFuente(),
                    request.getUrl(),
                    request.getPathApi(),
                    request.getEmail(),
                    request.getPassword()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(fuente);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<FuenteDemoDTO>> listarFuentesDemo() {
        List<FuenteDemoDTO> fuentes = fuentesDemoService.listarFuentesDemo();
        if (fuentes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(fuentes);
    }
}

