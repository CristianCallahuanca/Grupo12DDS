package org.example.metamapa.agregador.controllers;

import dinamico.models.entidades.hecho.HechoCrudo;
import dinamico.models.repositorios.IRepositorioHechosCrudos;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("agregador")
public class IntegracionController {

    //esta logica no es 100% correcta queria algo para testear los hechos del agregador

    private final IRepositorioHechos hechosRepository;

    public IntegracionController(IRepositorioHechos hechosRepository){
        this.hechosRepository = hechosRepository;
    }


    @GetMapping("/hechos")
    List<Hecho> hechosCrudos() {
        return hechosRepository.obtenerTodosLosHechosDelSistema();
    }

}
