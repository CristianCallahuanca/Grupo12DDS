package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.EstCantidadSolicitudesSpam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IEstCantidadSolicitudesSpamRepository extends JpaRepository<EstCantidadSolicitudesSpam, Long> {

    EstCantidadSolicitudesSpam findTopByOrderByFechaCalculoDesc();

    List<EstCantidadSolicitudesSpam> findByFechaCalculoBetween(LocalDateTime desde, LocalDateTime hasta);
}
