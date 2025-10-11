package org.example.metamapa.agregador.controllers;

import org.example.metamapa.agregador.models.dtos.DTO_IN.FuenteDTO;
import org.example.metamapa.agregador.service.IFuentesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fuentes")
public class FuentesController {

    private final IFuentesService fuentesService;

    public FuentesController(IFuentesService fuentesService) {
        this.fuentesService = fuentesService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarFuente(@RequestBody FuenteDTO fuenteDTO) {
        fuentesService.registrarFuente(fuenteDTO);
        return ResponseEntity.ok("Fuente registrada correctamente");
    }
}

