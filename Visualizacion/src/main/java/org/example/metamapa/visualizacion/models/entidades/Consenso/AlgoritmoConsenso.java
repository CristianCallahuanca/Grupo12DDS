package org.example.metamapa.visualizacion.models.entidades.Consenso;

import org.example.metamapa.visualizacion.models.entidades.Hecho;
import java.util.ArrayList;
import java.util.List;

public interface AlgoritmoConsenso {

    // List<Fuente> obtenerFuentesDelSistema();

    void verificar(List<Hecho> hechos);

    boolean esConsensuado(Hecho hecho, List <Fuente> Fuentes);
}
