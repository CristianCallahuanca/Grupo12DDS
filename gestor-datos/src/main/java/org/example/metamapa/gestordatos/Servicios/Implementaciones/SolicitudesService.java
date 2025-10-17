package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.infraestructura.IDetectorDeSpam;
import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.SolicitudEliminacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.example.metamapa.gestordatos.models.repositorios.ISolicitudesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudesService implements ISolicitudesService {

    private final ISolicitudesRepository solicitudRepository;
    private final IHechosRepository hechosRepository;
    private final IDetectorDeSpam DetectorDeSpam;


    @Override
    public SolicitudOutputDTO crearSolicitudEliminacion(SolicitudInputDTO dto) {
        Hecho hecho = hechosRepository.findById(dto.getIdhecho())
                .orElseThrow(() -> new IllegalArgumentException("Hecho no encontrado con ID: " + dto.getIdhecho()));

        SolicitudEliminacion solicitud = new SolicitudEliminacion(hecho, dto.getJustificacion());

        // Detección de spam
        if (DetectorDeSpam.esSpam(solicitud.getJustificacion())) {
            solicitud.setEstadoEliminar(EstadoEliminar.RECHAZADA);
            solicitud.setVerificoSiEsSpam(true);
        }

        solicitudRepository.save(solicitud);
        return toOutputDTO(solicitud);
    }


    @Override
    public SolicitudOutputDTO aprobarSolicitud(Long id) {
        SolicitudEliminacion solicitud = solicitudRepository.findById(id).orElse(null);
        if (solicitud == null) return null;

        solicitud.setEstadoEliminar(EstadoEliminar.APROBADA);
        solicitudRepository.save(solicitud);

        return toOutputDTO(solicitud);
    }

    @Override
    public SolicitudOutputDTO denegarSolicitud(Long id) {
        SolicitudEliminacion solicitud = solicitudRepository.findById(id).orElse(null);
        if (solicitud == null) return null;

        solicitud.setEstadoEliminar(EstadoEliminar.RECHAZADA);
        solicitudRepository.save(solicitud);

        return toOutputDTO(solicitud);
    }

    @Override
    public List<SolicitudOutputDTO> listarSolicitudesPendientes() {
        return solicitudRepository.findByEstadoEliminar(EstadoEliminar.PENDIENTE)
                .stream()
                .map(this::toOutputDTO)
                .toList();
    }



    private SolicitudOutputDTO toOutputDTO(SolicitudEliminacion solicitud) {
        SolicitudOutputDTO dto = new SolicitudOutputDTO();
        dto.setEstado(solicitud.getEstadoEliminar());
        dto.setJustificacion(solicitud.getJustificacion());
        dto.setIdHechoAsociado(solicitud.getHecho().getHecho_id());
        return dto;
    }
}

