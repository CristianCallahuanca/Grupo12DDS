package org.example.metamapa.agregador.controllers;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("agregador")
public class IntegracionController {



    private final IRepositorioHechos hechosRepository;

    public IntegracionController( IRepositorioHechos hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    @GetMapping("/hechos")
    List<Hecho> hechosCrudos() {
        //return hechosRepository.obtenerTodosLosHechosDelSistema();
        return hechosRepository.findAll();
    }

    @PostMapping("/solicitud_eliminacion/cancelar/{id}")
    void cancelarSolicitud(@PathVariable Long id){
        this.serviceSolicitud.cancelarSolicitud(id);
    }


}
