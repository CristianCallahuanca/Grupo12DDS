package org.example.metamapa.publica.service;

import org.example.metamapa.publica.models.dtos.input.FiltroDTO;
import org.example.metamapa.publica.models.dtos.input.ModoNavegacionDTO;
import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;

import java.util.List;

public interface INavegacionService {
    List<HechoOutputDTO> obtenerHechosDeColeccion(String idColeccion);
    List<HechoOutputDTO> navegarFiltrada(FiltroDTO filtro);
    List<HechoOutputDTO> navegarModo(String idColeccion, ModoNavegacionDTO modo);
}
