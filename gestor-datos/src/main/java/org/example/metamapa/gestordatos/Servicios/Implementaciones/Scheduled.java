package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IScheduled;
import org.springframework.beans.factory.annotation.Autowired;

public class Scheduled implements IScheduled {

    private final IColeccionesService coleccionesService;

    public Scheduled(IColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    @Override
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 3 * * *")
    public void consensuarHechos(){
        System.out.println("HECHOS CONSENSUADOS");
        coleccionesService.aplicarConsensoATodas();
    }

}
