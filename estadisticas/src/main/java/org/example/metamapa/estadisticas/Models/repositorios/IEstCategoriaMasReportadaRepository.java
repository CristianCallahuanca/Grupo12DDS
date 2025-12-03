package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.EstCategoriaMasReportada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IEstCategoriaMasReportadaRepository extends JpaRepository<EstCategoriaMasReportada, Long> {

    EstCategoriaMasReportada findTopByOrderByFechaCalculoDesc();

    List<EstCategoriaMasReportada> findByFechaCalculoBetween(LocalDateTime desde, LocalDateTime hasta);
}
