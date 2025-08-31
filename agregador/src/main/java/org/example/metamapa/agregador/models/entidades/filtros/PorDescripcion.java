package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

import java.util.Objects;

public class PorDescripcion implements FilterCondition {
    private String fraseClave;

    public PorDescripcion(String keyPhrase){
        this.fraseClave = keyPhrase;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return  unHecho.getDescripcion().toLowerCase()
                .contains(this.fraseClave.toLowerCase());
    }
}
