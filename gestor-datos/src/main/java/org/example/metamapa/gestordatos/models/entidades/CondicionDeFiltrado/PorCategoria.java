package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.Objects;

public class PorCategoria implements CondicionDeFiltrado {
    private String categoriaDeseada;

    public PorCategoria(String keyPhrase){
        this.categoriaDeseada = keyPhrase;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getCategoria(), categoriaDeseada);
    }

}
