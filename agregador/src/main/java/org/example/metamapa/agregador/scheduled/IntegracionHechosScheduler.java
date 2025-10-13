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

    public IntegracionHechosScheduler(IAgregacionService agregacionService) {
        this.agregacionService = agregacionService;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void obtenerHechosTodasLasFuentes() {
        log.info("Iniciando integración de hechos desde todas las fuentes registradas...");
        agregacionService.integrarHechosFuentes();
        log.info("Integración completada correctamente.");
    }
}

