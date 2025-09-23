package org.example.metamapa.gestordatos.Controladores;


import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gestordatos")
public class HechosController {

    private final IHechoService hechosService;

    public HechosController(IHechoService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping("/hechos")
    public List<HechoOutputDTO> obtenerHechos
            (@RequestParam(required = false) String categoria,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_reporte_desde,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_reporte_hasta,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_acontecimiento_desde,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_acontecimiento_hasta,
             @RequestParam(required = false) Double latitud,
             @RequestParam(required = false) Double longitud
             ){

        return hechosService.buscarTodosLosHechos(categoria, fecha_reporte_desde, fecha_reporte_hasta, fecha_acontecimiento_desde,
                fecha_acontecimiento_hasta, latitud, longitud);
    }

}
