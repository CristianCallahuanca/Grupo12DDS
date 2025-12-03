package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.EstProvinciaPorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IEstProvinciaPorCategoriaRepository extends JpaRepository<EstProvinciaPorCategoria, Long> {

    List<EstProvinciaPorCategoria> findByFechaCalculo(LocalDateTime fechaCalculo);

    @Query("SELECT MAX(e.fechaCalculo) FROM EstProvinciaPorCategoria e")
    LocalDateTime findMaxFechaCalculo();


    List<EstProvinciaPorCategoria> findByFechaCalculoBetween(
            LocalDateTime desde, LocalDateTime hasta);
}
