package org.example.metamapa.loaderdemo.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.loaderdemo.service.ICargadorHechosService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledCargarHechos {

    private final ICargadorHechosService cargadorHechosService;

    @Scheduled(cron = "0 0 */6 * * *")
    public void ejecutarCargaProgramada() {
        log.info("[Scheduler] Ejecutando carga de hechos desde todas las fuentes demo...");
        cargadorHechosService.cargarHechosDeTodasLasFuentes();
    }
}
