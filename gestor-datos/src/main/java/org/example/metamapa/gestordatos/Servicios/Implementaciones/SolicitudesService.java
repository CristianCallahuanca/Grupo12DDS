package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.DetectorDeSpamSingleton;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.SolicitudEliminacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.example.metamapa.gestordatos.models.repositorios.ISolicitudesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudesService implements ISolicitudesService {

    private ISolicitudesRepository solicitudRepository;
    private IHechosRepository hechosRepository;

    public SolicitudesService(ISolicitudesRepository solicitudRepository, IHechosRepository hechosRepository) {
        this.solicitudRepository = solicitudRepository;
        this.hechosRepository = hechosRepository;
    }

    private SolicitudEliminacion convertirInputDTOASolicitud(SolicitudInputDTO solicitudDTO){
        Hecho unHecho = hechosRepository.findById(solicitudDTO.getIDhecho()).orElse(null);
        if(unHecho == null){
            return null;
        }
        return new SolicitudEliminacion(unHecho, solicitudDTO.getJustificacion());
    }

    private SolicitudOutputDTO solicitudAOutpuDTO(SolicitudEliminacion solicitud){
        SolicitudOutputDTO dto = new SolicitudOutputDTO();
        dto.setEstado(solicitud.getEstadoEliminar());
        dto.setJustificacion(solicitud.getJustificacion());
        dto.setIdHechoAsociado(solicitud.getHecho().getHecho_id());
        return dto;
    }

    public SolicitudOutputDTO crearSolicitudEliminacion(SolicitudInputDTO solicitudInputDTO) {
        SolicitudEliminacion solicitud = convertirInputDTOASolicitud(solicitudInputDTO);

        if(solicitud == null){
            return null;
        }

        if(DetectorDeSpamSingleton.getInstance().esSpam(solicitud.getJustificacion())){
            solicitud.setEstadoEliminar(EstadoEliminar.RECHAZADA);
            solicitud.setVerifico_si_es_spam(true);
        }
        solicitudRepository.save(solicitud);

        return this.solicitudAOutpuDTO(solicitud);
    }

    public SolicitudOutputDTO aprobarSolicitud(Long id) {
        SolicitudEliminacion solicitud = solicitudRepository.findById(id).orElse(null);
        if (solicitud == null) {
            return null;
        }
        solicitud.setEstadoEliminar(EstadoEliminar.APROBADA);
        solicitudRepository.save(solicitud);

        return this.solicitudAOutpuDTO(solicitud);
    }

    public SolicitudOutputDTO denegarSolicitud(Long id) {
        SolicitudEliminacion solicitud = solicitudRepository.findById(id).orElse(null);
        if (solicitud == null) {
            return null;
        }
        solicitud.setEstadoEliminar(EstadoEliminar.RECHAZADA);
        solicitudRepository.save(solicitud);

        return this.solicitudAOutpuDTO(solicitud);
    }

}
