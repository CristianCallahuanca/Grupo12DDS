package org.example.metamapa.gestordatos.models.entidades.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

public abstract class AlgoritmoConsenso {
    // List<Fuente> obtenerFuentesDelSistema();

   // public void verificar(List<Hecho> hechos);

    public abstract boolean esConsensuado(Hecho hecho);
}
