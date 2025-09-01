package org.example.metamapa.publica.controllers;

import org.example.metamapa.publica.models.dtos.input.FiltroDTO;
import org.example.metamapa.publica.models.dtos.input.ModoNavegacionDTO;
import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.publica.service.INavegacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/navegacion")
public class NavegacionController {

    private final INavegacionService navegacionService;

    public NavegacionController(INavegacionService navegacionService) {
        this.navegacionService = navegacionService;
    }

    // Body → POST (GET no debe llevar body)
    @PostMapping("/filtrada")
    public ResponseEntity<List<HechoOutputDTO>> navegarConFiltro(@RequestBody FiltroDTO filtro) {
        return ResponseEntity.ok(navegacionService.navegarFiltrada(filtro));
    }

    // Objeto complejo → body; además semánticamente es navegación por “modo”
    @PostMapping("/coleccion/{id}/modo")
    public ResponseEntity<List<HechoOutputDTO>> navegarModo(
            @PathVariable String id,
            @RequestBody ModoNavegacionDTO modo
    ) {
        return ResponseEntity.ok(navegacionService.navegarModo(id, modo));
    }
}
