package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.HechosPorProvincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IMayorCantidadHechosProvincia extends JpaRepository<HechosPorProvincia, Long> {

    @Query(value = """
            SELECT ROW_NUMBER() OVER() as id, c.titulo, u.provincia, COUNT(h.hecho_id) as cantidad_hechos
            FROM hecho as h
            JOIN ubicacion as u ON h.hecho_id = u.id
            JOIN hecho_de_coleccion as hc ON hc.hecho_id = h.hecho_id
            JOIN coleccion as c ON hc.coleccion_id = c.handle
            GROUP BY c.titulo, u.provincia
            ORDER BY cantidad_hechos DESC
            LIMIT 1;
    """, nativeQuery = true)

    HechosPorProvincia findTopProvinciaColeccion();
}
