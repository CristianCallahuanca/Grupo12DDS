package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.Ubicacion;

public class PorUbicacion implements CondicionDeFiltrado{

    private final Ubicacion ubicacionBuscada;

    public PorUbicacion(Ubicacion ubicacion1) {
        ubicacionBuscada = ubicacion1;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getUbicacion().equals(ubicacionBuscada);

    }
}
