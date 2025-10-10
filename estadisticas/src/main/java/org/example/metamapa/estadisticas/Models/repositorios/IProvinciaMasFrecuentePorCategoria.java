package org.example.metamapa.estadisticas.Models.repositorios;

import org.example.metamapa.estadisticas.Models.entidades.EstadisticaGeneral;
import org.example.metamapa.estadisticas.Models.entidades.HechosPorProvincia;
import org.example.metamapa.estadisticas.Models.entidades.ProvinciaMasFrecuentePorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinciaMasFrecuentePorCategoria extends JpaRepository<ProvinciaMasFrecuentePorCategoria, Long> {
    @Query(value = """
            WITH categorias AS (
                        SELECT 'vientos fuertes' as categoria
                        UNION SELECT 'inundaciones'
                        UNION SELECT 'granizo'
                        UNION SELECT 'nevadas'
                        UNION SELECT 'calor extremo'
                        UNION SELECT 'sequía'
                        UNION SELECT 'derrumbes'
                        UNION SELECT 'actividad volcánica'
                        UNION SELECT 'incendios'
                        UNION SELECT 'contaminación'
                        UNION SELECT 'evento sanitario'
                        UNION SELECT 'derrame'
                        UNION SELECT 'intoxicación masiva'
                    ),
                    maximos_por_categoria AS (
                        SELECT
                            h.categoria,
                            u.provincia,
                            COUNT(*) as cantidad
                        FROM hecho h
                        JOIN ubicacion u ON h.ubicacion_id = u.id
                        WHERE h.categoria IN (SELECT categoria FROM categorias)
                        GROUP BY h.categoria, u.provincia
                    ),
                    provincia_maxima AS (
                        SELECT
                            m1.categoria,
                            m1.provincia,
                            m1.cantidad
                        FROM maximos_por_categoria m1
                        WHERE m1.cantidad = (
                            SELECT MAX(m2.cantidad)
                            FROM maximos_por_categoria m2
                            WHERE m2.categoria = m1.categoria
                        )
                    )
                    SELECT
                        c.categoria,
                        COALESCE(pm.provincia, 'Sin datos') as provincia,
                        COALESCE(pm.cantidad, 0) as cantidad
                    FROM categorias c
                    LEFT JOIN provincia_maxima pm ON c.categoria = pm.categoria
                    ORDER BY c.categoria;
            
    """, nativeQuery = true)
    ProvinciaMasFrecuentePorCategoria findTopProvinciaPorCategoria();

}
