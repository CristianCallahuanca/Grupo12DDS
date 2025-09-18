package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.EstadoHecho;
import org.example.metamapa.visualizacion.models.entidades.Hecho;

public class CriterioPorEstado implements ICriterioPertenencia {
    private EstadoHecho estado;
    public CriterioPorEstado(EstadoHecho estado) { this.estado = estado; }
    @Override public boolean cumple(Hecho h) { return h.getEstadoHecho() == estado; }
}
