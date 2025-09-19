package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.Ubicacion;

public class PorUbicacion extends CondicionDeFiltrado {
    private final Ubicacion ubicacionBuscada;

    public PorUbicacion(Ubicacion ubicacion1) {
        ubicacionBuscada = ubicacion1;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getUbicacion().equals(ubicacionBuscada);

    }
}
