package org.example.metamapa.estatico.service;

import org.example.metamapa.estatico.models.dtos.HechoDTO;

import java.util.List;

public interface IHechosService {
    List<HechoDTO> obtenerHechos();
}
