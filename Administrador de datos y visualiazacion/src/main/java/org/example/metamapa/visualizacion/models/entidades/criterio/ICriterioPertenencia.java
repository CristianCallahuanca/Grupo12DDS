package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

public interface ICriterioPertenencia {
    boolean cumple(Hecho hecho);
}

