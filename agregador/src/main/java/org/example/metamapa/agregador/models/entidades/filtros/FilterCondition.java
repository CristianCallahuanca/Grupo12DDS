package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

public interface FilterCondition {
    public boolean cumpleUno(Hecho unHecho);
}
