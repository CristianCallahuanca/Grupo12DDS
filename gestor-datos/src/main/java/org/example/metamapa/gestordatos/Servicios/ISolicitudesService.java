package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;

public interface ISolicitudesService {

    public SolicitudOutputDTO aprobarSolicitud(Long id);

    public SolicitudOutputDTO denegarSolicitud(Long id);
}
