package org.example.metamapa.visualizacion.models.entidades.navegacion;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

import java.util.List;
import java.util.stream.Collectors;

public class Curada implements IModoNavegacion {

    @Override
    public List<Hecho> aplicarAFiltrados(List<Hecho> hechosFiltrados) {
        return hechosFiltrados.stream()
                .filter(Hecho::esVisible)
                .collect(Collectors.toList());
    }
}
