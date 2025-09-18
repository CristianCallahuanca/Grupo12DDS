package org.example.metamapa.visualizacion.models.entidades.algoritmo;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

import java.util.List;

public abstract class AlgoritmoConsenso {
    public abstract List<Hecho> aplicar(List<Hecho> hechos);
}
