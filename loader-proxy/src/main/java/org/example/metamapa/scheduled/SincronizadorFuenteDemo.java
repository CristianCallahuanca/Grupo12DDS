package org.example.metamapa.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.service.fuentes.FuenteDemo;
import org.example.metamapa.service.fuentes.FuenteProxyFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SincronizadorFuenteDemo {

    private final FuenteProxyFactory factory;

    public SincronizadorFuenteDemo(FuenteProxyFactory factory) {
        this.factory = factory;
    }

    @Scheduled(fixedRate = 3600000)
    public void sincronizar() {
        FuenteDemo demo = factory.getFuenteDemo();
        if (demo != null) {
            log.info("Ejecutando sincronizacion automatica de FuenteDemo...");
            demo.actualizarHechosDesdeFuente();
        } else {
            log.warn("No se pudo sincronizar FuenteDemo porque aún no fue instanciada");
        }
    }
}
