package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;

public class PorIDHecho extends CondicionDeFiltrado{
    private final long idBuscado;

    public PorIDHecho(long id) {
        this.idBuscado = id;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getId() == (idBuscado);
    }
}
