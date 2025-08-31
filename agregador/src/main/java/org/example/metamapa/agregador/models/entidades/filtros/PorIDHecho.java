package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

public class PorIDHecho {
    private final String idBuscado;

    public PorIDHecho(String id) {
        this.idBuscado = id;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getId_hecho().equals(idBuscado);
    }
}
