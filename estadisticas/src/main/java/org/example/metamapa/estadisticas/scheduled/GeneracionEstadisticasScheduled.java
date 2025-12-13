package org.example.metamapa.estadisticas.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GeneracionEstadisticasScheduled {

    private final IEstadisticaService estadisticaService;

    public GeneracionEstadisticasScheduled(IEstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    //@Scheduled(initialDelay = 0, fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void crearEstadisticas() {
        log.info("Iniciando generación automática de estadísticas...");
        try {
            estadisticaService.generarEstadisticas();
            log.info("Estadísticas generadas correctamente.");
        } catch (Exception e) {
            log.error("Error durante la generación automática de estadísticas", e);
        }
    }
}
