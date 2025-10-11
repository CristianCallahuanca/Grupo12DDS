package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.ProvinciaMasFrecuentePorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProvinciaMasFrecuentePorCategoria extends JpaRepository<ProvinciaMasFrecuentePorCategoria, Long> {

        @Query(value = """
        SELECT 
            ROW_NUMBER() OVER() as id,
            h.categoria,
            u.provincia,
            COUNT(*) as cantidad
        FROM hecho h
        JOIN ubicacion u ON h.ubicacion_id = u.id
        WHERE h.categoria = 'buenos aires'
        GROUP BY h.categoria, u.provincia
        ORDER BY cantidad DESC
        LIMIT 1
    """, nativeQuery = true)
        ProvinciaMasFrecuentePorCategoria findTopProvinciaPorCategoria(@Param("categoria") String categoria);


}

