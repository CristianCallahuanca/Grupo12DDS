package org.example.metamapa.gestordatos.models.entidades.ModosNavegacion;

import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;
import org.example.metamapa.gestordatos.models.entidades.Consenso.AlgoritmoConsenso;

import java.util.List;

public class Curada implements ModoNavegacion {

    @Override
    public List<Hecho> aplicarModoDeNavegacion(Coleccion coleccion) {

        return coleccion.obtenerHechosConsensuados();
    }

}
