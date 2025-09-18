package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

public class PorDescripcion implements CondicionDeFiltrado{
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
