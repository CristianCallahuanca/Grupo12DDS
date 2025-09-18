package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

public class PorIDHecho implements CondicionDeFiltrado{
    private final long idBuscado;

    public PorIDHecho(long id) {
        this.idBuscado = id;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getId() == (idBuscado);
    }
}
