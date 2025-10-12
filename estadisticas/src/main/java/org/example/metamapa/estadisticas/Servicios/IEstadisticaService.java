package org.example.metamapa.estadisticas.Servicios;

import org.example.metamapa.estadisticas.Models.entidades.EstadisticaGeneral;

import java.util.List;

public interface IEstadisticaService {

    public void generarEstadisticas();
    public List<EstadisticaGeneral> obtenerEstadisticas();
}
