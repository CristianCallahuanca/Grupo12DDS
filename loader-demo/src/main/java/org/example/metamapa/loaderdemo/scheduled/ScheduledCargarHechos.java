package org.example.metamapa.loaderdemo.scheduled;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.service.ICargadorHechosService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledCargarHechos {

    private final ICargadorHechosService cargadorHechosService;

    @Scheduled(cron = "0 0 * * * *") // cada hora, en punto
    public void ejecutarCargaProgramada() {
        System.out.println("[Scheduler] Ejecutando carga de hechos desde fuente demo...");
        cargadorHechosService.cargarSiguienteHecho();
    }
}
