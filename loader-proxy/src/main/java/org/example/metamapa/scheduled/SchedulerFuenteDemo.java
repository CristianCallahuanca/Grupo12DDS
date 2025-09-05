package org.example.metamapa.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.service.fuentes.FuenteDemo;
import org.example.metamapa.service.fuentes.FuenteProxyFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SchedulerFuenteDemo {

    private final FuenteProxyFactory factory;

    public SchedulerFuenteDemo(FuenteProxyFactory factory) {
        this.factory = factory;
    }

    @Scheduled(initialDelay = 0, fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void obtenerHechosFuenteDemo() {
        FuenteDemo demo = factory.getFuenteDemo();
        if (demo != null) {
            log.info("Ejecutando sincronizacion automatica de FuenteDemo...");
            demo.actualizarHechosDesdeFuente();
        } else {
            log.warn("No se pudo sincronizar FuenteDemo porque aún no fue instanciada");
        }
    }
}
