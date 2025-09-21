package org.example.metamapa.gestordatos.models.entidades.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;

import java.util.List;

public abstract class AlgoritmoConsenso {
    public abstract boolean esConsensuado(Hecho hecho);

    public void consensuarHechos(List<HechoDeColeccion> hechosDeColeccion) {
        for (HechoDeColeccion hechoColeccion : hechosDeColeccion) {
            Hecho hecho = hechoColeccion.getHecho(); //no me andan los getters
            boolean consensuado = esConsensuado(hecho);
            hechoColeccion.setEsConsensuado(consensuado);
        }
    } //TODO ESTO LO TIENE QUE HACER UN SCHEDULER, VER BIEN DÓNDE LO ACTUALIZA AL REPO
}
