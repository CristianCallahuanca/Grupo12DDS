package org.example.metamapa.gestordatos.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsensoScheduler {

    private final IColeccionesService coleccionesService;
    private static boolean enEjecucion = false;

    public ConsensoScheduler(IColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    /**
     * Ejecuta el consenso sobre todas las colecciones todos los días a las 03:00 AM.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public synchronized void ejecutarConsensoDiario() {
        if (enEjecucion) {
            log.warn("Ya hay un proceso de consenso en ejecución. Se omite esta instancia.");
            return;
        }

        enEjecucion = true;
        log.info("Iniciando tarea programada: consenso diario de colecciones (03:00 AM).");

        try {
            coleccionesService.aplicarConsensoATodas();
            log.info("Consenso diario completado exitosamente.");
        } catch (Exception e) {
            log.error("Error durante el consenso diario: {}", e.getMessage(), e);
        } finally {
            enEjecucion = false;
        }
    }
}
