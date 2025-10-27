package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;

import java.util.List;

public abstract class AlgoritmoConsenso {
    public abstract boolean esConsensuado(Hecho hecho, List<Hecho> hechosDeColeccion);
    public abstract String getNombre();
    public void consensuarHechos(List<HechoDeColeccion> hechosDeColeccion) {
        List<Hecho> hechos = hechosDeColeccion.stream()
                .map(HechoDeColeccion::getHecho)
                .toList();

        for (HechoDeColeccion hechoColeccion : hechosDeColeccion) {
            Hecho hecho = hechoColeccion.getHecho();
            boolean consensuado = esConsensuado(hecho, hechos);
            hechoColeccion.setConsensuado(consensuado);
        }
    }
}
