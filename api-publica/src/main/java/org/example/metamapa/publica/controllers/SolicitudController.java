package org.example.metamapa.publica.controllers;

import org.example.metamapa.publica.models.dtos.input.SolicitudDTO;
import org.example.metamapa.publica.service.ISolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final ISolicitudService solicitudService;

    public SolicitudController(ISolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<Void> generarSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        solicitudService.generarSolicitud(solicitudDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
