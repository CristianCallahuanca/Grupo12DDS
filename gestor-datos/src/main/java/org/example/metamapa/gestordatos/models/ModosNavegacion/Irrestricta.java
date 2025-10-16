package org.example.metamapa.gestordatos.models.ModosNavegacion;

import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.List;

public class Irrestricta implements ModoNavegacion{

    @Override
    public List<Hecho> aplicarModoDeNavegacion(Coleccion coleccion){
        return coleccion.obtenerHechosVisibles();
    }
}
