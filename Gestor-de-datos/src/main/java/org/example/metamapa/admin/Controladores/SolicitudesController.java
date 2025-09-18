package org.example.metamapa.admin.Controladores;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.admin.Servicios.ISolicitudesService;
import org.example.metamapa.admin.models.dtos.output.SolicitudOutputDTO;
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
