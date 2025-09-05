package org.example.metamapa.agregador.scheduled;


import dinamico.models.repositorios.IRepositorioHechosCrudos;
import org.example.metamapa.agregador.models.dtos.HechoDTO;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.service.implementacion.AgregacionService;
import org.example.metamapa.agregador.service.implementacion.DuplicacionService;
import org.example.metamapa.agregador.service.implementacion.NormalizacionService;
import org.example.metamapa.common.HechoDTOCommon;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class IntegracionScheduled {

    private final AgregacionService agregacionService;

    public IntegracionScheduled (AgregacionService agregacionService){
        this.agregacionService = agregacionService;
    }

    @Scheduled(initialDelay = 0, fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void obtenerHechosTodasLasFuentes(){
        System.out.println("LLAME A TODAS LAS FUENTES");
        agregacionService.integrarHechosFuentes();
    }

}










