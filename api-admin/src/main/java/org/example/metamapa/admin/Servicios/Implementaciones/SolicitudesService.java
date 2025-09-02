package org.example.metamapa.admin.Servicios.Implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.admin.Servicios.ISolicitudesService;
import org.example.metamapa.admin.models.dtos.output.SolicitudOutputDTO;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SolicitudesService implements ISolicitudesService {

    @Override
    public SolicitudOutputDTO aprobarSolicitud(Long id) {
        log.info("Aprobando solicitud con id {}", id);
        return new SolicitudOutputDTO(); // Implementación futura
    }

    @Override
    public SolicitudOutputDTO denegarSolicitud(Long id) {
        log.info("Denegando solicitud con id {}", id);
        return new SolicitudOutputDTO(); // Implementación futura
    }
}
