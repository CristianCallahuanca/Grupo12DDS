package org.example.metamapa.publica.service.implementaciones;

import org.example.metamapa.publica.clientes.AdministradorClient;
import org.example.metamapa.publica.models.dtos.input.SolicitudDTO;
import org.example.metamapa.publica.service.ISolicitudService;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService implements ISolicitudService {

    private final AdministradorClient adminClient;

    public SolicitudService(AdministradorClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    public void generarSolicitud(SolicitudDTO solicitud) {
        adminClient.enviarSolicitudDeEliminacion(solicitud);
    }
}

