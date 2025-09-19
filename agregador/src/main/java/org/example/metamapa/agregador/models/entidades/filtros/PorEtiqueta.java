package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;
import java.util.Objects;

public class PorEtiqueta extends CondicionDeFiltrado {
    private String etiquetaDeseada;

    public PorEtiqueta(String etiquetaDeseada) {
        this.etiquetaDeseada = etiquetaDeseada;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getEtiqueta(), etiquetaDeseada);
    }

}
