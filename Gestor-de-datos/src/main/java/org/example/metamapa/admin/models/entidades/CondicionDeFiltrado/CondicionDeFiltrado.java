package org.example.metamapa.admin.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.admin.models.entidades.Hecho;

public interface CondicionDeFiltrado {
    public boolean cumpleUno(Hecho unHecho);
}
