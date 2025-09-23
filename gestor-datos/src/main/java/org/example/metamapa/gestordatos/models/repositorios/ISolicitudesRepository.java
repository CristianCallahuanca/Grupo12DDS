package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.SolicitudEliminacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudesRepository extends JpaRepository<SolicitudEliminacion, Long> {
    List<SolicitudEliminacion> findByEstadoEliminar(EstadoEliminar estadoEliminar); //Falopeada de hibernate
}

