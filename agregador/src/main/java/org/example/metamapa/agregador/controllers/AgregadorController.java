package org.example.metamapa.agregador.controllers;

import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.service.IAgregacionService;
import org.example.metamapa.agregador.service.IDuplicacionService;
import org.example.metamapa.agregador.service.INormalizacionService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("agregador")
public class AgregadorController {

    /*//private final IRepositorioHechos hechosRepository;

    private final IAgregacionService agregador_service;

    public AgregadorController(IAgregacionService agregador_service){
        this.agregador_service = agregador_service;
    }

    @GetMapping("/hechos-agregados")
    List<HechoDTO_IN> hechosAgregados() {
        return this.agregador_service.getHechosDTO3FuentesSinLimpiar();
    }*/
}
