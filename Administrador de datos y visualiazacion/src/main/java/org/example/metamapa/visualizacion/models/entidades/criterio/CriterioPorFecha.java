package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

import java.time.LocalDateTime;

public class CriterioPorFecha implements ICriterioPertenencia {

    private LocalDateTime desde;
    private LocalDateTime hasta;

    public CriterioPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    @Override
    public boolean cumple(Hecho hecho) {
        return hecho.getFechaAcontecimiento().isAfter(desde)
                && hecho.getFechaAcontecimiento().isBefore(hasta);
    }
}
