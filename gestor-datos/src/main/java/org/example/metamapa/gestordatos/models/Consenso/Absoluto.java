package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import java.util.List;

public class Absoluto extends AlgoritmoConsenso{

    @Override
    public String getNombre() {
        return "Absoluto";
    }
    @Override
    public boolean esConsensuado(Hecho hecho, List<Hecho> hechosDeColeccion) {
        var grupo = hechosDeColeccion.stream()
                .filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()))
                .toList();

        long fuentesDistintas = grupo.stream()
                .map(Hecho::getOrigenReal)
                .distinct()
                .count();

        long totalFuentesColeccion = hechosDeColeccion.stream()
                .map(Hecho::getOrigenReal)
                .distinct()
                .count();

        return fuentesDistintas == totalFuentesColeccion;
    }
}
