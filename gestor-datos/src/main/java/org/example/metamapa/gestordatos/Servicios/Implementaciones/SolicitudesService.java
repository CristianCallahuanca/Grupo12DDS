package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.DetectorDeSpamSingleton;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.SolicitudEliminacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;
import org.example.metamapa.gestordatos.models.repositorios.ISolicitudesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudesService implements ISolicitudesService {

    private ISolicitudesRepository solicitudRepository;

    public SolicitudesService(ISolicitudesRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    private SolicitudEliminacion convertirInputDTOASolicitud(SolicitudInputDTO solicitudDTO){
        //Hecho unHecho =
        //return new SolicitudEliminacion(solicitudDTO.getIDhecho(), solicitudDTO.getJustificacion());
    }

    private SolicitudOutputDTO convertirOutputDTOASolicitud(SolicitudEliminacion solicitud){
        SolicitudOutputDTO dto = new SolicitudOutputDTO();
        dto.setEstado(solicitud.getEstadoEliminar());
        dto.setJustificacion(solicitud.getJustificacion());
        return dto;
    }

    public SolicitudOutputDTO crearSolicitudEliminacion(SolicitudInputDTO solicitudInputDTO) {
        SolicitudEliminacion solicitud = convertirInputDTOASolicitud(solicitudInputDTO);

        if(DetectorDeSpamSingleton.getInstance().esSpam(solicitud.getJustificacion())){
            solicitud.setEstadoEliminar(EstadoEliminar.RECHAZADA);
            solicitud.setVerifico_si_es_spam(true);
        }
        solicitudRepository.save(solicitud);

        return this.convertirOutputDTOASolicitud(solicitud);
    }

    public SolicitudOutputDTO aprobarSolicitud(Long id) {
        SolicitudEliminacion solicitud = solicitudRepository.findById(id).orElse(null);
        if (solicitud == null) {
            return null;
        }
        solicitud.setEstadoEliminar(EstadoEliminar.APROBADA);

        return new SolicitudOutputDTO(id, solicitud.getEstadoEliminar(), solicitud.getJustificacion(), solicitud.getHecho().getHecho_id());
    }

    ;

    public SolicitudOutputDTO denegarSolicitud(Long id) {
        SolicitudEliminacion solicitud = solicitudRepository.findById(id).orElse(null);
        if (solicitud == null) {
            return null;
        }
        solicitud.setEstadoEliminar(EstadoEliminar.RECHAZADA);

        return new SolicitudOutputDTO(id, solicitud.getEstadoEliminar(), solicitud.getJustificacion(), solicitud.getHecho().getHecho_id());
    }

}
