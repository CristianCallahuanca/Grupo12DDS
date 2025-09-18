package org.example.metamapa.visualizacion.models.entidades.navegacion;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

import java.util.List;

public class Irrestricta implements IModoNavegacion {

    @Override
    public List<Hecho> aplicarAFiltrados(List<Hecho> hechosFiltrados) {
        return hechosFiltrados; // no filtra por visibilidad
    }
}
