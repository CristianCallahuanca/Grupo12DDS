package org.example.metamapa.agregador;

import org.example.metamapa.common.HechoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class AgregadorController {
    private final AgregacionService svc;
    AgregadorController(AgregacionService s){ this.svc = s; }

    @GetMapping("/hechos-aggregados")
    List<HechoDTO> hechosAggregados(){ return svc.agregar(); }
}