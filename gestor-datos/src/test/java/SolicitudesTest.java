import org.example.metamapa.gestordatos.Servicios.Implementaciones.SolicitudesService;
import org.example.metamapa.gestordatos.infraestructura.IDetectorDeSpam;
import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.SolicitudEliminacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.example.metamapa.gestordatos.models.repositorios.ISolicitudesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SolicitudesTest {

    @Mock
    ISolicitudesRepository solicitudesRepo;
    @Mock
    IHechosRepository hechosRepo;
    @Mock
    IDetectorDeSpam detectorSpam;

    @InjectMocks
    SolicitudesService service;

    @Test
    void crearSolicitud_valida() {
        // setup
        Hecho hecho = new Hecho();
        hecho.setHecho_id(10L);

        SolicitudInputDTO dto = new SolicitudInputDTO();
        dto.setIdhecho(10L);
        dto.setJustificacion("Este hecho contiene información falsa");

        when(hechosRepo.findById(10L)).thenReturn(Optional.of(hecho));
        when(detectorSpam.esSpam(anyString())).thenReturn(false);

        // ejecución
        SolicitudOutputDTO out = service.crearSolicitudEliminacion(dto);

        // verificación
        assertThat(out.getEstado()).isEqualTo(EstadoEliminar.PENDIENTE);
        verify(solicitudesRepo).save(any(SolicitudEliminacion.class));
        verify(detectorSpam).esSpam("Este hecho contiene información falsa");
    }

    @Test
    void crearSolicitud_esSpam_rechazada() {
        Hecho hecho = new Hecho();
        hecho.setHecho_id(11L);

        SolicitudInputDTO dto = new SolicitudInputDTO();
        dto.setIdhecho(11L);
        dto.setJustificacion("Gana dinero rápido $$$ hazte rico");

        when(hechosRepo.findById(11L)).thenReturn(Optional.of(hecho));
        when(detectorSpam.esSpam(anyString())).thenReturn(true);

        SolicitudOutputDTO out = service.crearSolicitudEliminacion(dto);

        assertThat(out.getEstado()).isEqualTo(EstadoEliminar.RECHAZADA);
        verify(detectorSpam).esSpam("Gana dinero rápido $$$ hazte rico");
        verify(solicitudesRepo).save(any(SolicitudEliminacion.class));
    }

    @Test
    void crearSolicitud_hechoInexistente() {
        SolicitudInputDTO dto = new SolicitudInputDTO();
        dto.setIdhecho(999L);
        dto.setJustificacion("Motivo válido");

        when(hechosRepo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.crearSolicitudEliminacion(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Hecho no encontrado con ID: 999");
    }


    @Test
    void listarSolicitudesPendientes() {
        SolicitudEliminacion s1 = new SolicitudEliminacion();
        s1.setEstadoEliminar(EstadoEliminar.PENDIENTE);
        s1.setJustificacion("Texto 1");
        Hecho h = new Hecho();
        h.setHecho_id(1L);
        s1.setHecho(h);

        when(solicitudesRepo.findByEstadoEliminar(EstadoEliminar.PENDIENTE))
                .thenReturn(List.of(s1));

        var out = service.listarSolicitudesPendientes();

        assertThat(out).hasSize(1);
        assertThat(out.get(0).getJustificacion()).isEqualTo("Texto 1");
        verify(solicitudesRepo).findByEstadoEliminar(EstadoEliminar.PENDIENTE);
    }


    @Test
    void aprobarSolicitud() {
        SolicitudEliminacion solicitud = new SolicitudEliminacion();
        solicitud.setEstadoEliminar(EstadoEliminar.PENDIENTE);

        Hecho hecho = new Hecho();
        hecho.setHecho_id(1L);
        solicitud.setHecho(hecho);

        when(solicitudesRepo.findById(5L)).thenReturn(Optional.of(solicitud));

        SolicitudOutputDTO out = service.aprobarSolicitud(5L);

        assertThat(out.getEstado()).isEqualTo(EstadoEliminar.APROBADA);
        verify(solicitudesRepo).save(solicitud);
    }


    @Test
    void rechazarSolicitud() {
        SolicitudEliminacion solicitud = new SolicitudEliminacion();
        solicitud.setEstadoEliminar(EstadoEliminar.PENDIENTE);

        Hecho hecho2 = new Hecho();
        hecho2.setHecho_id(2L);
        solicitud.setHecho(hecho2);

        when(solicitudesRepo.findById(7L)).thenReturn(Optional.of(solicitud));

        SolicitudOutputDTO out = service.denegarSolicitud(7L);

        assertThat(out.getEstado()).isEqualTo(EstadoEliminar.RECHAZADA);
        verify(solicitudesRepo).save(solicitud);
    }

    @Test
    void aprobarSolicitud_inexistente_retornaNull() {
        when(solicitudesRepo.findById(55L)).thenReturn(Optional.empty());

        var out = service.aprobarSolicitud(55L);

        assertThat(out).isNull();
        verify(solicitudesRepo).findById(55L);
    }

    @Test
    void rechazarSolicitud_inexistente_retornaNull() {
        when(solicitudesRepo.findById(77L)).thenReturn(Optional.empty());

        var out = service.denegarSolicitud(77L);

        assertThat(out).isNull();
        verify(solicitudesRepo).findById(77L);
    }

}
