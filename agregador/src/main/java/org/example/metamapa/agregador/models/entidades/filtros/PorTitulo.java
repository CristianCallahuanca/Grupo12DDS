package org.example.metamapa.agregador.models.entidades.filtros;

import org.example.metamapa.agregador.models.entidades.Hecho;

import java.util.Objects;

public class PorTitulo extends CondicionDeFiltrado{
    private final String tituloBuscado;

    public PorTitulo(String titulo) {
        this.tituloBuscado = titulo;
    }

    public boolean cumpleUno(Hecho unHecho) {
        return  Objects.equals(unHecho.getTitulo(), tituloBuscado);
    }

}
