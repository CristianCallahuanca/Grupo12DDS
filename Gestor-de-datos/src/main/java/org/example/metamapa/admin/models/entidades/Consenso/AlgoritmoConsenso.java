package org.example.metamapa.admin.models.entidades.Consenso;

import org.example.metamapa.admin.models.entidades.Hecho;

import java.util.List;

public interface AlgoritmoConsenso {
    // List<Fuente> obtenerFuentesDelSistema();

   // public void verificar(List<Hecho> hechos);

    boolean esConsensuado(Hecho hecho);
}
