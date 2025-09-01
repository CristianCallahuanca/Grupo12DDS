package org.example.metamapa.publica.service.implementaciones;

import org.example.metamapa.publica.clientes.AgregadorClient;
import org.example.metamapa.publica.models.dtos.input.SolicitudDTO;
import org.example.metamapa.publica.service.ISolicitudService;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService implements ISolicitudService {

    private final AgregadorClient agregadorClient;

    public SolicitudService(AgregadorClient agregadorClient) {
        this.agregadorClient = agregadorClient;
    }

    @Override
    public void generarSolicitud(SolicitudDTO solicitud) {
        agregadorClient.enviarSolicitudDeEliminacion(solicitud);
    }
}
