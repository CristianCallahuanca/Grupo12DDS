package org.example.metamapa.agregador;

import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.service.implementacion.AgregacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class AgregadorController {
    private final AgregacionService svc;
    AgregadorController(AgregacionService s){ this.svc = s; }

    @GetMapping("/hechos-agregados")
    List<HechoDTO_IN> hechosAgregados(){ return svc.getHechosDTO3FuentesSinLimpiar(); }
}