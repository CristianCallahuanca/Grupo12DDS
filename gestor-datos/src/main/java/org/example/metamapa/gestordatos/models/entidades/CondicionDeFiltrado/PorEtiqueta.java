package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.Objects;

public class PorEtiqueta implements CondicionDeFiltrado {
    private String etiquetaDeseada;

    public PorEtiqueta(String etiquetaDeseada) {
        this.etiquetaDeseada = etiquetaDeseada;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getEtiqueta(), etiquetaDeseada);
    }


}
