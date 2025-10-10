package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.CategoriaMasFrecuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaMasFrecuente extends JpaRepository<CategoriaMasFrecuente, Long> {

    @Query(value = """
        SELECT
            ROW_NUMBER() OVER() as id,
            categoria,
            COUNT(*) as cantidad
        FROM hecho
        WHERE categoria != 'Sin categoria'
        GROUP BY categoria
        ORDER BY cantidad DESC
        LIMIT 1
        """, nativeQuery = true)

    CategoriaMasFrecuente findCategoriaMasFrecuente();
}
