package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.List;
import java.util.Objects;


public class MultiplesMenciones extends AlgoritmoConsenso {

    @Override
    public String getNombre() {
        return "Múltiples Menciones";
    }
    @Override
    public boolean esConsensuado(Hecho hecho, List<Hecho> hechosDeColeccion) {
        // Agrupamos por título
        var grupo = hechosDeColeccion.stream()
                .filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()))
                .toList();

        long fuentesDistintas = grupo.stream()
                .map(Hecho::getOrigenReal)
                .distinct()
                .count();

        // Si hay al menos 2 fuentes distintas, y no hay conflictos entre atributos
        boolean variasFuentes = fuentesDistintas > 1;
        boolean hayConflictos = grupo.stream().anyMatch(h1 ->
                grupo.stream().anyMatch(h2 ->
                        !h1.equals(h2) &&
                                (!Objects.equals(h1.getDescripcion(), h2.getDescripcion()) ||
                                        !Objects.equals(h1.getCategoria(), h2.getCategoria()) ||
                                        !Objects.equals(h1.getFechaAcontecimiento(), h2.getFechaAcontecimiento()))
                )
        );

        return variasFuentes && !hayConflictos;
    }

}


