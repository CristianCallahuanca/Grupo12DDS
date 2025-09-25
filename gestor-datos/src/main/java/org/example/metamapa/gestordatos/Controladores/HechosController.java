package org.example.metamapa.gestordatos.Controladores;


import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gestordatos")
public class HechosController {

    private final IHechoService hechosService;

    public HechosController(IHechoService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoOutputDTO>> obtenerHechos(@RequestBody List<CriterioRequest> criterios){

        return ResponseEntity.status(200).body(hechosService.buscarTodosLosHechos(criterios));
    }

}
