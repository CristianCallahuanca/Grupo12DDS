package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.Objects;

public class PorSinCategorizar implements CondicionDeFiltrado{

    private final boolean sinCategorizar;

    public PorSinCategorizar(boolean situacion){
        this.sinCategorizar = situacion;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getSinCategorizar(), sinCategorizar);
    }

}
