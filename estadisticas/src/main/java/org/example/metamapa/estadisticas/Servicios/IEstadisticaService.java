package org.example.metamapa.estadisticas.Servicios;

import org.example.metamapa.estadisticas.Models.entidades.CategoriaMasFrecuente;
import org.example.metamapa.estadisticas.Models.entidades.HechosPorProvincia;
import org.example.metamapa.estadisticas.Models.entidades.ProvinciaMasFrecuentePorCategoria;

import java.util.List;

public interface IEstadisticaService {

    void generarEstadisticas();
    HechosPorProvincia generarEstadisticaMayorCantHechosProvincia();
    CategoriaMasFrecuente generarEstadisticaMayorCantHechosCategoria();
    ProvinciaMasFrecuentePorCategoria generarEstadisticaProvinciaMasFrecuentePorCategoria();
    //public void generarEstadisticaHoraDelDia();
    //public void generarEstadisticaCantidadSolicitudesSpam();
}
