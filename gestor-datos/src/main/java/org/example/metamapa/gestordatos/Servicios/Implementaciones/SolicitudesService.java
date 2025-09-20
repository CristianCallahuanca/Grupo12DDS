package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.springframework.stereotype.Service;

@Service
public class SolicitudesService implements ISolicitudesService {

    public SolicitudOutputDTO aprobarSolicitud(Long id){
        return new SolicitudOutputDTO();
    };

    public SolicitudOutputDTO denegarSolicitud(Long id){
        return new SolicitudOutputDTO();
    };

    /*public void buscarSpam() {
        List<SolicitudEliminacion> solicitudesPendientes = this.solicitudesRepository.findByEstadoEliminar(EstadoEliminar.PENDIENTE);

        for (SolicitudEliminacion s : solicitudesPendientes) {
            if(DetectorDeSpamSingleton.getInstance().esSpam(s.getJustificacion())){
                s.setEstadoEliminar(EstadoEliminar.RECHAZADA);
                s.setVisible(false);
                this.solicitudesRepository.save(s);
            }
        }
    }*/

}
