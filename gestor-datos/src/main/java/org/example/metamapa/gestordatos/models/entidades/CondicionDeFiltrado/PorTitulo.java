package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.Objects;

public class PorTitulo implements CondicionDeFiltrado{
    private final String tituloBuscado;

    public PorTitulo(String titulo) {
        this.tituloBuscado = titulo;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  Objects.equals(unHecho.getTitulo(), tituloBuscado);
    }

}
