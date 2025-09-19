package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

public abstract class CondicionDeFiltrado {

    public abstract boolean cumpleUno(Hecho unHecho);
}
