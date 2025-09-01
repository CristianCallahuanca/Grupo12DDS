package org.example.metamapa.publica.service;

import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IColeccionService {
    List<HechoOutputDTO> obtenerHechosDeColeccion(String idColeccion);
}

