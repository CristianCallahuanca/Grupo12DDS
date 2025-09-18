package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.clientes.AdministracionClient;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
@Slf4j
public class SolicitudesService implements ISolicitudesService {

    private final AdministracionClient administracionClient;

    @Override
    public SolicitudOutputDTO aprobarSolicitud(Long id) {
        log.info("Aprobando solicitud con id {}", id);
        return administracionClient.aprobarSolicitud(id);
    }

    @Override
    public SolicitudOutputDTO denegarSolicitud(Long id) {
        log.info("Denegando solicitud con id {}", id);
        return administracionClient.denegarSolicitud(id);
    }
}

