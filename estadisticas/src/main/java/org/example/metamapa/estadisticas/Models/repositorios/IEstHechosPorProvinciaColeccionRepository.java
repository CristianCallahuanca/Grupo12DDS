package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.EstHechosPorProvinciaColeccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IEstHechosPorProvinciaColeccionRepository extends JpaRepository<EstHechosPorProvinciaColeccion, Long> {

    EstHechosPorProvinciaColeccion findTopByOrderByFechaCalculoDesc();

    List<EstHechosPorProvinciaColeccion> findByFechaCalculoBetween(
            LocalDateTime desde, LocalDateTime hasta);

}
