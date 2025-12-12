package org.example.metamapa.estatico.scheduled;

import jakarta.annotation.PostConstruct;
import org.example.metamapa.estatico.service.IProcesadorCsvService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronProcesadorCSV {

    private final IProcesadorCsvService procesador;

    public CronProcesadorCSV(IProcesadorCsvService procesador) {
        this.procesador = procesador;
    }

    @PostConstruct
    public void cargaInicial() {
        procesador.procesarFuentesPendientes();
    }

    @Scheduled(cron = "0 0 * * * *") // cada 1h
    public void ejecucionPeriodica() {
        procesador.procesarFuentesPendientes();
    }
}


