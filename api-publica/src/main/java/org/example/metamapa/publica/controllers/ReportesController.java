package org.example.metamapa.publica.controllers;

import org.example.metamapa.publica.models.dtos.input.ReporteDTO;
import org.example.metamapa.publica.service.IReportesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportes")
public class ReportesController {

    private final IReportesService reportesService;

    public ReportesController(IReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @PostMapping
    public ResponseEntity<Void> reportarHecho(@RequestBody ReporteDTO reporteDTO) {
        reportesService.reportarHecho(reporteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

