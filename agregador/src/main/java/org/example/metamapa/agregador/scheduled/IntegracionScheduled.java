package org.example.metamapa.agregador.scheduled;


import org.example.metamapa.agregador.service.implementacion.AgregacionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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










