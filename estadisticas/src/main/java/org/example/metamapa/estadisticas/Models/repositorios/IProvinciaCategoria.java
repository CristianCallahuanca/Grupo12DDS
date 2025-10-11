package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.ProvinciaCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProvinciaCategoria extends JpaRepository<ProvinciaCategoria, Long> {

    // Opción 1: Usando @Procedure (RECOMENDADO)
    @Procedure(procedureName = "obtener_provincia_que_mas_uso_la_categoria")
    List<Object[]> ejecutarProcedureCategoria(@Param("categoria_param") String categoria);

    // Opción 2: Usando @Query con CALL
    @Query(value = "CALL obtener_provincia_que_mas_uso_la_categoria(:categoria)", nativeQuery = true)
    List<Object[]> findProvinciaPorCategoria(@Param("categoria") String categoria);
}