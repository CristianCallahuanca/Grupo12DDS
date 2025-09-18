package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.Hecho;
import org.example.metamapa.visualizacion.models.entidades.Origen;

public class CriterioPorOrigen implements ICriterioPertenencia {
    private Origen origen;
    public CriterioPorOrigen(Origen origen) { this.origen = origen; }
    @Override public boolean cumple(Hecho h) { return h.getOrigen() == origen; }
}
