package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.List;
import java.util.Objects;


public class MultiplesMenciones extends AlgoritmoConsenso {
    private int minimoFuentesRequeridas = 2;
    @Override
    public String getNombre() {
        return "Múltiples Menciones";
    }
    @Override
    public boolean esConsensuado(Hecho hecho, List<Hecho> hechosDeColeccion) {
        // Agrupamos por título
        var grupo = hechosDeColeccion.stream()
                .filter(h -> sonCercanosEntreSi(hecho, h)) //Obtengo hechos con ubicacion aproximada, pero que pueden tener distinto contenido
                .toList();

        if(grupo.size() <= 1) return false;

        long fuentesDistintas = grupo.stream()
                .map(h -> h.getOrigenReal().getTipoFuente())
                .distinct()
                .count();

        // Si hay al menos 2 fuentes distintas, y no hay conflictos entre atributos
        if(fuentesDistintas < minimoFuentesRequeridas) return false;

        boolean hayConflictos = grupo.stream().anyMatch(h1 ->
                grupo.stream().anyMatch(h2 ->
                        !Objects.equals(h1.getHecho_id(), h2.getHecho_id()) &&
                                !tienenElMismoContenido(h1, h2)
                )
        );

        return !hayConflictos;
    }

    private boolean sonCercanosEntreSi(Hecho h1, Hecho h2){
        return this.distanciaMetros(h1, h2) <= RADIO_RAZONABLE_METROS;
    }

}


