package org.example.metamapa.gestordatos.models.entidades.ModosNavegacion;


import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;

import java.util.List;

public interface ModoNavegacion {
    //El segundo parametro es un Object por temas de extensibilidad/escalabilidad
    public List<Hecho> aplicarModoDeNavegacion (List<HechoDeColeccion> hechos, Object modo);
}
