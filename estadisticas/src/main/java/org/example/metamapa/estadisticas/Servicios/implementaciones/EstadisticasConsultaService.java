package org.example.metamapa.estadisticas.Servicios.implementaciones;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.estadisticas.Models.dtos.*;
import org.example.metamapa.estadisticas.Models.entidades.*;
import org.example.metamapa.estadisticas.Models.repositorios.*;
import org.example.metamapa.estadisticas.Servicios.IEstadisticasConsultaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadisticasConsultaService implements IEstadisticasConsultaService {

    private final IEstHechosPorProvinciaColeccionRepository repoMayorHechos;
    private final IEstCategoriaMasReportadaRepository repoCatMasRep;
    private final IEstProvinciaPorCategoriaRepository repoProvPorCat;
    private final IEstHoraPorCategoriaRepository repoHoraPorCat;
    private final IEstCantidadSolicitudesSpamRepository repoSpam;

    // Helpers

    private LocalDateTime inicio(LocalDate d) {
        return d.atStartOfDay();
    }

    private LocalDateTime fin(LocalDate d) {
        return d.atTime(LocalTime.MAX);
    }

    // ─────────────────────────────
    // 1) Mayor cantidad de hechos por provincia y colección
    // ─────────────────────────────
    @Override
    public List<EstadMayorHechosPorProvinciaColeccionDTO> obtenerMayorHechosProvinciaColeccion() {

        LocalDateTime maxFecha = repoMayorHechos.findMaxFechaCalculo();
        if (maxFecha == null) return List.of();

        return repoMayorHechos.findByFechaCalculo(maxFecha).stream()
                .map(e -> new EstadMayorHechosPorProvinciaColeccionDTO(
                        e.getFechaCalculo(),
                        e.getColeccionTitulo(),
                        e.getProvincia(),
                        e.getCantidadHechos()
                ))
                .toList();
    }

    // ─────────────────────────────
    // 2) Categoría más reportada
    // ─────────────────────────────
    @Override
    public List<EstadCategoriaMasReportadaDTO> obtenerCategoriaMasReportada() {

            EstCategoriaMasReportada e = repoCatMasRep.findTopByOrderByFechaCalculoDesc();
            if (e == null) return List.of();
            return List.of(new EstadCategoriaMasReportadaDTO(
                    e.getFechaCalculo(),
                    e.getCategoriaNombre(),
                    e.getCantidadHechos()
            ));

    }

    // ─────────────────────────────
    // 3) Provincia por categoría
    // ─────────────────────────────
    @Override
    public List<EstadProvinciaPorCategoriaDTO> obtenerProvinciaPorCategoria() {

            LocalDateTime maxFecha = repoProvPorCat.findMaxFechaCalculo();
            if (maxFecha == null) return List.of();

            return repoProvPorCat.findByFechaCalculo(maxFecha).stream()
                    .map(e -> new EstadProvinciaPorCategoriaDTO(
                            e.getFechaCalculo(),
                            e.getCategoriaNombre(),
                            e.getProvincia(),
                            e.getCantidadHechos()
                    ))
                    .toList();


    }

    // ─────────────────────────────
    // 4) Hora por categoría
    // ─────────────────────────────
    @Override
    public List<EstadHoraPorCategoriaDTO> obtenerHoraPorCategoria() {


            LocalDateTime maxFecha = repoHoraPorCat.findMaxFechaCalculo();
            if (maxFecha == null) return List.of();

            return repoHoraPorCat.findByFechaCalculo(maxFecha).stream()
                    .map(e -> new EstadHoraPorCategoriaDTO(
                            e.getFechaCalculo(),
                            e.getCategoriaNombre(),
                            e.getHora(),
                            e.getCantidadHechos()
                    ))
                    .toList();

    }

    // ─────────────────────────────
    // 5) Cantidad de solicitudes spam
    // ─────────────────────────────
    @Override
    public List<EstadCantidadSolicitudesSpamDTO> obtenerCantidadSolicitudesSpam() {


            EstCantidadSolicitudesSpam e = repoSpam.findTopByOrderByFechaCalculoDesc();
            if (e == null) return List.of();
            return List.of(new EstadCantidadSolicitudesSpamDTO(
                    e.getFechaCalculo(),
                    e.getCantidadSpam()
            ));


    }
}
