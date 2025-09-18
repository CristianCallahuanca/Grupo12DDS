package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;

public interface ISolicitudesService {

    SolicitudOutputDTO aprobarSolicitud(Long id);

    SolicitudOutputDTO denegarSolicitud(Long id);
}
