package org.example.metamapa.estadisticas.Servicios;

import org.example.metamapa.estadisticas.Models.entidades.CategoriaMasFrecuente;
import org.example.metamapa.estadisticas.Models.entidades.HechosPorProvincia;

public interface IEstadisticaService {

    void generarEstadisticas();
    HechosPorProvincia generarEstadisticaMayorCantHechosProvincia();
    CategoriaMasFrecuente generarEstadisticaMayorCantHechosCategoria();
    //public void generarEstadisticaMayorCantCategoriaProvincia();
    //public void generarEstadisticaHoraDelDia();
    //public void generarEstadisticaCantidadSolicitudesSpam();
}
