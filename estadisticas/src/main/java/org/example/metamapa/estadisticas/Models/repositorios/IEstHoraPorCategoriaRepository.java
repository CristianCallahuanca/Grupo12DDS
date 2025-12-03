package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.EstHoraPorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IEstHoraPorCategoriaRepository  extends JpaRepository<EstHoraPorCategoria, Long> {

    List<EstHoraPorCategoria> findByFechaCalculo(LocalDateTime fechaCalculo);

    @Query("SELECT MAX(e.fechaCalculo) FROM EstHoraPorCategoria e")
    LocalDateTime findMaxFechaCalculo();


    List<EstHoraPorCategoria> findByFechaCalculoBetween(
            LocalDateTime desde, LocalDateTime hasta);
}
