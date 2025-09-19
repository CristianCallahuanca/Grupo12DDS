package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;

import java.util.Objects;

public class PorTitulo extends CondicionDeFiltrado{
    private final String tituloBuscado;

    public PorTitulo(String titulo) {
        this.tituloBuscado = titulo;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  Objects.equals(unHecho.getTitulo(), tituloBuscado);
    }

}
