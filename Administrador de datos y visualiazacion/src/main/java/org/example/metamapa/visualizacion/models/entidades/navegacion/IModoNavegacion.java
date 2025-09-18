package org.example.metamapa.visualizacion.models.entidades.navegacion;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

import java.util.List;

public interface IModoNavegacion {
    List<Hecho> aplicarAFiltrados(List<Hecho> hechosFiltrados);
}
