package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.Origen;

public class PorOrigen extends CondicionDeFiltrado{
    private Origen unOrigen;

    public PorOrigen(Origen origen) {
        this.unOrigen = origen;
    }

    public boolean cumpleUno(Hecho unHecho){
        return unHecho.getOrigenes().contains(unOrigen);
    }
}
