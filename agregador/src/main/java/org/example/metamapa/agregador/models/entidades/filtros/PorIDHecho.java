package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

public class PorIDHecho extends CondicionDeFiltrado{
    private final Long idBuscado;

    public PorIDHecho(Long id) {
        this.idBuscado = id;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getId() == idBuscado;
    }
}
