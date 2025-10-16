package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;

import java.util.List;

public interface ISolicitudesService {

    public SolicitudOutputDTO crearSolicitudEliminacion(SolicitudInputDTO solicitudInputDTO);

    public SolicitudOutputDTO aprobarSolicitud(Long id);

    public SolicitudOutputDTO denegarSolicitud(Long id);

    List<SolicitudOutputDTO> listarSolicitudesPendientes();
}
