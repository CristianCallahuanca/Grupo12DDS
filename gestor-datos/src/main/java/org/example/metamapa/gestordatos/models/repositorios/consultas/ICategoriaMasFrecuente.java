package org.example.metamapa.gestordatos.models.repositorios.consultas;

import org.example.metamapa.gestordatos.models.entidades.consultas.CategoriaMasFrecuente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface IMayorCategoriaFrecuente extends JpaRepository<CategoriaMasFrecuente, Long> {

    @Query(value = """
        SELECT 
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