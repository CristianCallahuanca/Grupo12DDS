package org.example.metamapa.gestordatos.Controladores;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudesController {

    private final ISolicitudesService solicitudesService;

    @PostMapping("/{id}/aprobar")
    public SolicitudOutputDTO aprobar(@PathVariable Long id) {
        return solicitudesService.aprobarSolicitud(id);
    }

    @PostMapping("/{id}/denegar")
    public SolicitudOutputDTO denegar(@PathVariable Long id) {
        return solicitudesService.denegarSolicitud(id);
    }
}
