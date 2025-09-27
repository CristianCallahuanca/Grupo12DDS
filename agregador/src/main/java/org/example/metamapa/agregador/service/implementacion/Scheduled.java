package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.service.IAgregacionService;
import org.example.metamapa.agregador.service.IScheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class Scheduled implements IScheduled {
    private final IAgregacionService agregacionService;

    public Scheduled (IAgregacionService agregacionService){
        this.agregacionService = agregacionService;
    }

    @org.springframework.scheduling.annotation.Scheduled(initialDelay = 0, fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void obtenerHechosTodasLasFuentes(){
        System.out.println("LLAME A TODAS LAS FUENTES");
        agregacionService.integrarHechosFuentes();
    }
}
