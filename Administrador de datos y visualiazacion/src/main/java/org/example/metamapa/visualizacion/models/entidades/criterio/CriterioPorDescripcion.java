package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

public class CriterioPorDescripcion implements ICriterioPertenencia {
    private String fraseClave;
    public CriterioPorDescripcion(String fraseClave) { this.fraseClave = fraseClave; }
    @Override public boolean cumple(Hecho h) {
        return h.getDescripcion() != null &&
                h.getDescripcion().toLowerCase().contains(fraseClave.toLowerCase());
    }
}
