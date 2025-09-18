package org.example.metamapa.admin.Servicios;

import org.example.metamapa.admin.models.dtos.output.SolicitudOutputDTO;

public interface ISolicitudesService {

    SolicitudOutputDTO aprobarSolicitud(Long id);

    SolicitudOutputDTO denegarSolicitud(Long id);
}
