package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.models.entidades.EstadoEliminar;
import org.example.metamapa.agregador.models.repositorios.ISpamRepository;
import org.example.metamapa.agregador.service.ISpamSolicitudes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.metamapa.agregador.models.entidades.SolicitudEliminacion;

import java.util.Optional;

@Service
public class SpamSolicitudes implements ISpamSolicitudes {

    private final ISpamRepository solicitudesRepository;

    public SpamSolicitudes(ISpamRepository solicitudesRepository) {
        this.solicitudesRepository = solicitudesRepository;
    }

    public void cancelarSolicitud(long id) {
        SolicitudEliminacion solicitud = this.solicitudesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la solicitud con id " + id));

        if (solicitud.getEstadoEliminar() != EstadoEliminar.RECHAZADA) {
            solicitud.setEstadoEliminar(EstadoEliminar.RECHAZADA);
            this.solicitudesRepository.delete(solicitud);
        }
    }
}
