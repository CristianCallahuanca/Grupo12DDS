package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.Ubicacion;

public class PorIDContribuyente extends CondicionDeFiltrado{
    private final long idBuscado;

    public PorIDContribuyente(long id) {
        this.idBuscado = id;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getContribuyente().getUserId() == idBuscado;
    }
}
