package org.example.metamapa.visualizacion.models.entidades.algoritmo;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsensoAbsoluto extends AlgoritmoConsenso {

    private int cantidadFuentesEsperadas;

    public ConsensoAbsoluto(int cantidadFuentesEsperadas) {
        this.cantidadFuentesEsperadas = cantidadFuentesEsperadas;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> hechos) {
        Map<String, List<Hecho>> agrupados = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getTitulo));

        return agrupados.values().stream()
                .filter(lista -> lista.size() == cantidadFuentesEsperadas)
                .map(lista -> lista.get(0))
                .collect(Collectors.toList());
    }
}
