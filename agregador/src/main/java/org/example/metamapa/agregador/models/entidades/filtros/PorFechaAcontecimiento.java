package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

import java.time.LocalDateTime;

public class PorFechaAcontecimiento implements FilterCondition {
    private LocalDateTime desde;
    private LocalDateTime hasta;

    public PorFechaAcontecimiento(LocalDateTime fa1, LocalDateTime fc1) {
        desde = fa1;
        hasta = fc1;
    }
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaAcontecimiento().isAfter(desde) || unHecho.getFechaAcontecimiento().isEqual(desde)) &&
                (unHecho.getFechaAcontecimiento().isBefore(hasta) || unHecho.getFechaAcontecimiento().isEqual(hasta));
    }
}
