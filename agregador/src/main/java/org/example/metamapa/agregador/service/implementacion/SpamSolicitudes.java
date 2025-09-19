package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.models.repositorios.ISpamRepository;
import org.example.metamapa.agregador.service.ISpamSolicitudes;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.example.metamapa.agregador.models.entidades.SolicitudEliminacion;

import java.util.Optional;

@Service
public class SpamSolicitudes {

    private final ISpamRepository solicitudesRepository;

    public SpamSolicitudes(ISpamRepository solicitudesRepository) {
        this.solicitudesRepository = solicitudesRepository;
    }

    void cancelarSolicitud(long id){
        Optional<SolicitudEliminacion> solicitud = this.solicitudesRepository.findById(id);

    }
}
