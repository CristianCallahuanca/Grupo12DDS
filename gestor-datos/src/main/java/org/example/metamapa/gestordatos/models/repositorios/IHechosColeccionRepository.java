package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHechosColeccionRepository extends JpaRepository<HechoDeColeccion, Long> {
    /*@Query("SELECT hc.hecho.id " +
            "FROM HechoDeColeccion hc " +
            "JOIN hc.coleccion c " +
            "WHERE c.handle = :handle")
    List<Long> findIdsHechosByColeccionHandle(@Param("handle") String handle);*/

    //List<HechoDeColeccion> findByC
}
