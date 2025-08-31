package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

import java.time.LocalDateTime;

public class PorFechaCarga implements FilterCondition {
    private LocalDateTime desde;
    private LocalDateTime hasta;

    public PorFechaCarga(LocalDateTime desde, LocalDateTime hasta) {
        desde = desde;
        hasta = hasta;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaCarga().isAfter(desde) || unHecho.getFechaCarga().isEqual(desde)) &&
                (unHecho.getFechaCarga().isBefore(hasta) || unHecho.getFechaCarga().isEqual(hasta));
    }
}
