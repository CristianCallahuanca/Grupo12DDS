package org.example.metamapa.agregador.controllers;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.service.ISpamSolicitudes;
import org.example.metamapa.agregador.service.implementacion.SpamSolicitudes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("agregador")
public class IntegracionController {


    private final ISpamSolicitudes serviceSolicitud;
    private final IRepositorioHechos hechosRepository;

    public IntegracionController(ISpamSolicitudes serviceSolicitud, IRepositorioHechos hechosRepository) {
        this.serviceSolicitud = serviceSolicitud;
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
