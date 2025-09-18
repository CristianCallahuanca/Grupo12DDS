package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

public class CriterioPorTitulo implements ICriterioPertenencia {
    private String tituloBuscado;
    public CriterioPorTitulo(String tituloBuscado) { this.tituloBuscado = tituloBuscado; }
    @Override public boolean cumple(Hecho h) {
        return h.getTitulo() != null &&
                h.getTitulo().toLowerCase().contains(tituloBuscado.toLowerCase());
    }
}
