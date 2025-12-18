package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.enums.TipoFuente;

import java.util.List;

public class Absoluto extends AlgoritmoConsenso{

    @Override
    public String getNombre() {
        return "Absoluto";
    }
    @Override
    public boolean esConsensuado(Hecho hecho, List<Hecho> hechosDeColeccion) {
        var grupo = hechosDeColeccion.stream()
                .filter(h -> this.tienenElMismoContenido(h, hecho))
                .toList();

        long fuentesDistintas = grupo.stream()
                .map(h -> h.getOrigenReal().getTipoFuente())
                .distinct()
                .count();

        return fuentesDistintas == TipoFuente.values().length;
    }
}
