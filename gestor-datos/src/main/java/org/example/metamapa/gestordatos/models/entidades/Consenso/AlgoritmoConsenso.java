package org.example.metamapa.gestordatos.models.entidades.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

public interface AlgoritmoConsenso {
    // List<Fuente> obtenerFuentesDelSistema();

   // public void verificar(List<Hecho> hechos);

    boolean esConsensuado(Hecho hecho);
}
