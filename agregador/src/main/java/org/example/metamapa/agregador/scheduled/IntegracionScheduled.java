package org.example.metamapa.agregador.scheduled;


import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.service.implementacion.AgregacionService;
import org.example.metamapa.agregador.service.implementacion.NormalizacionService;
import org.example.metamapa.common.HechoDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class IntegracionScheduled {

    private final AgregacionService agregacionService;
    private final NormalizacionService normalizacionService;

    public IntegracionScheduled(AgregacionService agregacionService, NormalizacionService normalizacionService) {
        this.agregacionService = agregacionService;
        this.normalizacionService = normalizacionService;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void obtenerHechosTodasLasFuentes(){

        List<HechoDTO> hechos = new ArrayList<>();

        hechos = agregacionService.agregar();

        

        //agregar la logica del normalizador


    }
}










