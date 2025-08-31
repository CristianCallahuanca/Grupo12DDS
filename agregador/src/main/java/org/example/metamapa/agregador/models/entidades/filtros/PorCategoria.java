package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

import java.util.Objects;

public class PorCategoria implements FilterCondition {
    private String categoriaDeseada;

    public PorCategoria(String keyPhrase){
        this.categoriaDeseada = keyPhrase;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getCategoria(), categoriaDeseada);
    }

}
