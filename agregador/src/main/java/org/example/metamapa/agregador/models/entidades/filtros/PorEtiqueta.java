package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

public class PorEtiqueta implements FilterCondition {
    private String etiquetaDeseada;

    public PorEtiqueta(String etiquetaDeseada) {
        this.etiquetaDeseada = etiquetaDeseada;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getEtiqueta(), etiquetaDeseada);
    }

}
