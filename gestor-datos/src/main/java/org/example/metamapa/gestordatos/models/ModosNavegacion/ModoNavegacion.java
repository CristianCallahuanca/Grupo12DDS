package org.example.metamapa.gestordatos.models.ModosNavegacion;


import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;

import java.util.List;

public interface ModoNavegacion {

    List<Hecho> aplicarModoDeNavegacion (Coleccion coleccion);
}
