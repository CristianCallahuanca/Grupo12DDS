package org.example.metamapa.gestordatos.models.repositorios;

import org.example.metamapa.gestordatos.models.entidades.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISpamRepository extends JpaRepository<SolicitudEliminacion, Long> {
    //List<SolicitudEliminacion> findByEstadoEliminar(EstadoEliminar estadoEliminar);
}

