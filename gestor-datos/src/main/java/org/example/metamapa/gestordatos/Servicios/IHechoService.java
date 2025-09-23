package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.HechoInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.time.LocalDate;
import java.util.List;

public interface IHechoService {

    public List<HechoOutputDTO> buscarTodosLosHechos(String categoria, LocalDate fecha_reporte_desde, LocalDate fecha_reporte_hasta,
                                                     LocalDate fecha_acontecimiento_desde, LocalDate fecha_acontecimiento_hasta,
                                                     Double latitud, Double longitud);
    public void guardarHecho(HechoInputDTO hechoInputDTO);
}
