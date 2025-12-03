package org.example.metamapa.estadisticas.Servicios;

import org.example.metamapa.estadisticas.Models.dtos.*;

import java.time.LocalDate;
import java.util.List;

public interface IEstadisticasConsultaService {

    List<EstadMayorHechosPorProvinciaColeccionDTO> obtenerMayorHechosProvinciaColeccion(LocalDate desde, LocalDate hasta);

    List<EstadCategoriaMasReportadaDTO> obtenerCategoriaMasReportada(LocalDate desde, LocalDate hasta);

    List<EstadProvinciaPorCategoriaDTO> obtenerProvinciaPorCategoria(LocalDate desde, LocalDate hasta);

    List<EstadHoraPorCategoriaDTO> obtenerHoraPorCategoria(LocalDate desde, LocalDate hasta);

    List<EstadCantidadSolicitudesSpamDTO> obtenerCantidadSolicitudesSpam(LocalDate desde, LocalDate hasta);
}
