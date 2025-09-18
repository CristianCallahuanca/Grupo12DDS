package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.HechoInputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.util.List;

public interface IHechoService {

    public List<Hecho> buscarTodos();
    public void guardarHecho(HechoInputDTO hechoInputDTO);
}
