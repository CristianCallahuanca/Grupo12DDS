package org.example.metamapa.agregador.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.service.IAgregacionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class IntegracionHechosScheduler {

    private final IAgregacionService agregacionService;
    private boolean ejecutando = false;

    public IntegracionHechosScheduler(IAgregacionService agregacionService) {
        this.agregacionService = agregacionService;
    }

    @Scheduled(cron = "${agregador.cron}")
    public void obtenerHechosTodasLasFuentes() {
        if (ejecutando) {
            log.warn("La integración anterior aún no finalizó. Se omite esta ejecución.");
            return;
        }
        try{
            log.info("Iniciando integración de hechos desde todas las fuentes registradas...");
            ejecutando = true;
            agregacionService.integrarHechosFuentes();
            log.info("Integración completada correctamente.");

        } finally {
            ejecutando = false;
        }
    }
}

