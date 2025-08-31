package org.example.metamapa.agregador.models.entidades.filtros;

import dinamico.models.entidades.hecho.EstadoHecho;
import org.example.metamapa.agregador.models.entidades.Hecho;

public class PorEstado implements FilterCondition{
    private EstadoHecho unEstado;

    public PorEstado(EstadoHecho unEstado){
        this.unEstado = unEstado;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  unHecho.getEstadoHecho().equals(this.unEstado);
    }
}
