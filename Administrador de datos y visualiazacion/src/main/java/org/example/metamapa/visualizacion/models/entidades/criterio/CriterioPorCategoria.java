package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

public class CriterioPorCategoria implements ICriterioPertenencia {

    private String categoriaDeseada;

    public CriterioPorCategoria(String categoriaDeseada) {
        this.categoriaDeseada = categoriaDeseada;
    }

    @Override
    public boolean cumple(Hecho hecho) {
        return hecho.getCategoria().equalsIgnoreCase(categoriaDeseada);
    }
}
