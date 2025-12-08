package org.example.metamapa.estadisticas.Servicios;

import org.example.metamapa.estadisticas.Models.dtos.*;

import java.time.LocalDate;
import java.util.List;

public interface IEstadisticasConsultaService {

    List<EstadMayorHechosPorProvinciaColeccionDTO> obtenerMayorHechosProvinciaColeccion();

    List<EstadCategoriaMasReportadaDTO> obtenerCategoriaMasReportada();

    List<EstadProvinciaPorCategoriaDTO> obtenerProvinciaPorCategoria();

    List<EstadHoraPorCategoriaDTO> obtenerHoraPorCategoria();

    List<EstadCantidadSolicitudesSpamDTO> obtenerCantidadSolicitudesSpam();
}
