package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.List;
import java.util.Objects;


public class MultiplesMenciones extends AlgoritmoConsenso {
    private static final int minimoFuentesRequeridas = 2;
    @Override
    public String getNombre() {
        return "Múltiples Menciones";
    }
    @Override
    public boolean esConsensuado(Hecho hecho, List<Hecho> hechosSimilares) {

        var grupoCercano = hechosSimilares.stream()
                .filter(h -> sonCercanosEntreSi(hecho, h)) //Obtengo hechos con ubicacion aproximada, pero que pueden tener distinto contenido
                .toList();

        if(grupoCercano.size() <= 1) return false;

        boolean hayConflicto = grupoCercano.stream()
                .anyMatch(h ->
                        h.getTitulo().equalsIgnoreCase(hecho.getTitulo()) &&
                                !tienenElMismoContenido(hecho, h)
                );

        if(hayConflicto){
            return false;
        }

        long fuentesQueAvalan = grupoCercano.stream()
                .filter(h -> tienenElMismoContenido(hecho, h))
                .map(h -> h.getOrigenReal().getTipoFuente())
                .distinct()
                .count() + 1;

        return fuentesQueAvalan >= minimoFuentesRequeridas;
    }

    private boolean sonCercanosEntreSi(Hecho h1, Hecho h2){
        return this.distanciaMetros(h1, h2) <= RADIO_RAZONABLE_METROS;
    }

}


