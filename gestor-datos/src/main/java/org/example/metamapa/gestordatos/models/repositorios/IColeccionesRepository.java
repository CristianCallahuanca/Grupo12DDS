package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IColeccionesRepository extends JpaRepository<Coleccion, String> {

}
