package org.example.metamapa.visualizacion.models.entidades.algoritmo;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiplesMenciones extends AlgoritmoConsenso {

    @Override
    public List<Hecho> aplicar(List<Hecho> hechos) {
        Map<String, List<Hecho>> agrupados = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getTitulo)); // Se agrupan por título como criterio simple

        return agrupados.values().stream()
                .filter(lista -> lista.size() > 1)
                .map(lista -> lista.get(0)) // Se queda con uno solo
                .collect(Collectors.toList());
    }
}
