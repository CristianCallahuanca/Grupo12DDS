package org.example.metamapa.visualizacion.models.entidades.algoritmo;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MayoriaSimple extends AlgoritmoConsenso {

    @Override
    public List<Hecho> aplicar(List<Hecho> hechos) {
        Map<String, List<Hecho>> agrupados = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getTitulo));

        int maxMenciones = agrupados.values().stream()
                .mapToInt(List::size)
                .max()
                .orElse(1);

        return agrupados.values().stream()
                .filter(lista -> lista.size() == maxMenciones)
                .map(lista -> lista.get(0))
                .collect(Collectors.toList());
    }
}
