package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

public class CriterioPorEtiqueta implements ICriterioPertenencia {

    private String etiqueta;

    public CriterioPorEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public boolean cumple(Hecho hecho) {
        return hecho.getEtiqueta().equalsIgnoreCase(etiqueta);
    }
}
