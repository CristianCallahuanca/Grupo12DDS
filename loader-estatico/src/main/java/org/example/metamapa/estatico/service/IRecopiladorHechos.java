package org.example.metamapa.estatico.service;

import org.example.metamapa.estatico.models.dtos.HechoDTO;

import java.io.IOException;
import java.util.List;

public interface IRecopiladorHechos {

    List<HechoDTO> obtenerHechosCrudos(int cantidad) throws IOException;
}
