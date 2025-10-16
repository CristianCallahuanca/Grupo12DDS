package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;

import java.util.List;

public interface IHechoColeccionService {

    public List<Long> obtenerIdsHechosAsociadosColeccion(String handle);
    public void actualizarHechosDeColeccion(List<HechoDeColeccion> hechos);
}
