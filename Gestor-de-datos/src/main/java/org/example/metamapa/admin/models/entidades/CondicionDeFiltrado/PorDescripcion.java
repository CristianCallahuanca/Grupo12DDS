package org.example.metamapa.admin.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.admin.models.entidades.Hecho;

public class PorDescripcion {
    private String fraseClave;

    public PorDescripcion(String keyPhrase){
        this.fraseClave = keyPhrase;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  unHecho.getDescripcion().toLowerCase()
                .contains(this.fraseClave.toLowerCase());
    }
}
