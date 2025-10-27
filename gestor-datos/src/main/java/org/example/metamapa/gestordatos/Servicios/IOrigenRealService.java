package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.output.OrigenRealDTO;
import org.example.metamapa.gestordatos.models.entidades.OrigenReal;

import java.util.List;

public interface IOrigenRealService {
    List<OrigenRealDTO> listarTodos();
}
