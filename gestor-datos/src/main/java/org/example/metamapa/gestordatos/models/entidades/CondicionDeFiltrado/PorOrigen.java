package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.enums.Origen;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;

public class PorOrigen extends CondicionDeFiltrado {
    private Origen unOrigen;

    public PorOrigen(Origen origen) {
        this.unOrigen = origen;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho){
        return unHecho.getOrigenes().contains(unOrigen);
    }
}
