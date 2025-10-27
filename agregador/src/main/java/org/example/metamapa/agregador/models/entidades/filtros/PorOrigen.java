package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.TipoFuente;

public class PorOrigen extends CondicionDeFiltrado {

    private TipoFuente unTipoFuente;

    public PorOrigen(TipoFuente tipoFuente) {
        this.unTipoFuente = tipoFuente;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getTipoFuente() == unTipoFuente;
    }
}
