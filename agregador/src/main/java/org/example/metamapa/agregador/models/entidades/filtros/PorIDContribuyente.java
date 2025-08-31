package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.Ubicacion;

public class PorIDContribuyente {
    private final String idBuscado;

    public PorIDContribuyente(String id) {
        this.idBuscado = id;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getContribuyente_id().equals(idBuscado);
    }
}
