package org.example.metamapa.estatico.service;

import org.example.metamapa.estatico.models.dtos.HechoCrudoDTO;

import java.io.IOException;
import java.util.List;

public interface IRecopiladorHechos {

    List<HechoCrudoDTO> obtenerHechosCrudos(int cantidad) throws IOException;
}
