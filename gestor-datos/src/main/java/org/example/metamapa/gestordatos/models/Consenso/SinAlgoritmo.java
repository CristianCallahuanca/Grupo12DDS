package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.List;

public class SinAlgoritmo extends AlgoritmoConsenso{
    @Override
    public String getNombre() {
        return "Sin Algoritmo";
    }
    @Override
    public boolean esConsensuado(Hecho hecho, List<Hecho> hechosSimilares) {
        return true;
    }

}
