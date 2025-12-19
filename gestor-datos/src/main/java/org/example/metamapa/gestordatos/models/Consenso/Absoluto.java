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
    public boolean esConsensuado(Hecho hecho, List<Hecho> hechosSimilares) {
        var grupo = hechosSimilares.stream()
                .filter(h -> this.tienenElMismoContenido(h, hecho))
                .toList();

        long fuentesDistintas = grupo.stream()
                .map(h -> h.getOrigenReal().getTipoFuente())
                .distinct()
                .count() + 1; // sumo uno porque en el grupo ya no hay hechos de la misma fuente que el hecho a consensuar

        return fuentesDistintas == TipoFuente.values().length;
    }
}
