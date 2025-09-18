package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

public interface CondicionDeFiltrado {
    public boolean cumpleUno(Hecho unHecho);
}
