package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.EstHechosPorProvinciaColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IEstHechosPorProvinciaColeccionRepository extends JpaRepository<EstHechosPorProvinciaColeccion, Long> {


    List<EstHechosPorProvinciaColeccion> findByFechaCalculoBetween(
            LocalDateTime desde, LocalDateTime hasta);

    @Query("select max(e.fechaCalculo) from EstHechosPorProvinciaColeccion e")
    LocalDateTime findMaxFechaCalculo();

    List<EstHechosPorProvinciaColeccion> findByFechaCalculo(LocalDateTime fechaCalculo);


}
