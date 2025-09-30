package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IEstadisticaService;
import org.example.metamapa.gestordatos.Servicios.IScheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class Scheduled implements IScheduled {

    private final IColeccionesService coleccionesService;
    private final IEstadisticaService estadisticaService;

    public Scheduled(IColeccionesService coleccionesService, IEstadisticaService estadisticaService) {
        this.coleccionesService = coleccionesService;
        this.estadisticaService = estadisticaService;
    }

    @Override
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 3 * * *")
    //@org.springframework.scheduling.annotation.Scheduled(initialDelay = 0, fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void consensuarHechos(){
        System.out.println("HECHOS CONSENSUADOS");
        coleccionesService.aplicarConsensoATodas(); //TODO: arreglar
    }

    @org.springframework.scheduling.annotation.Scheduled(initialDelay = 0, fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void crearEstadisticas(){
        System.out.println("SE ESTAN GENERANDO NUEVAS ESTADISTICAS");
        estadisticaService.generarEstadisticas();

    }

}
