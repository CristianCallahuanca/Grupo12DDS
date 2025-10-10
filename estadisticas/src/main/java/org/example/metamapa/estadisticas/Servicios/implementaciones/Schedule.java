package org.example.metamapa.estadisticas.Servicios.implementaciones;

import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.example.metamapa.estadisticas.Servicios.ISchedule;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class Schedule implements ISchedule {

    private final IEstadisticaService estadisticaService;

    public Schedule(IEstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @org.springframework.scheduling.annotation.Scheduled(initialDelay = 0, fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void crearEstadisticas(){
        System.out.println("SE ESTAN GENERANDO NUEVAS ESTADISTICAS");
        estadisticaService.generarEstadisticas();
    }
}
