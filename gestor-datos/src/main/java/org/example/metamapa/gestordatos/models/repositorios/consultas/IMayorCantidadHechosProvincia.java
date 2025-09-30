package org.example.metamapa.gestordatos.models.repositorios.consultas;

import org.example.metamapa.gestordatos.models.entidades.consultas.HechosPorProvincia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface IMayorCantidadHechosProvincia extends JpaRepository<HechosPorProvincia, Long>{

    @Query(value = """
        SELECT ROW_NUMBER() OVER() as id, c.titulo, u.provincia, COUNT(distinct h.hecho_id) as cantidad_hechos 
        FROM hecho as h 
        JOIN ubicacion as u ON h.ubicacion_id = u.id
        JOIN hecho_de_coleccion as hc ON hc.hecho_id = h.hecho_id
        JOIN coleccion as c ON hc.coleccion_id = c.handle
        GROUP BY c.titulo, u.provincia
        ORDER BY cantidad_hechos DESC
        LIMIT 1
        """, nativeQuery = true)
    HechosPorProvincia findTopProvinciaColeccion();
}
