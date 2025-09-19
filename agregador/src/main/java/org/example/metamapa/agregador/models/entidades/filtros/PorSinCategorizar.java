package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

import java.io.Serializable;
import java.util.Objects;

public class PorSinCategorizar extends CondicionDeFiltrado{
    private final boolean sinCategorizar;

    public PorSinCategorizar(boolean situacion){
        this.sinCategorizar = situacion;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getSinCategorizar(), sinCategorizar);
    }

}
