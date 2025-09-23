package org.example.metamapa.gestordatos.Controladores;

import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.springframework.http.ResponseEntity;
import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestordatos")
public class SolicitudesController {

    private final ISolicitudesService solicitudesService;

    SolicitudesController(ISolicitudesService solicitudesService){
        this.solicitudesService = solicitudesService;
    }

    @PostMapping("/solicitud")
    public ResponseEntity<String> create(@RequestBody SolicitudInputDTO solicitud) {
        SolicitudOutputDTO solictud = this.solicitudesService.crearSolicitudEliminacion(solicitud);
        return ResponseEntity.ok("El hecho se subio con exito");
    }

    @PostMapping("/{id}/aprobar")
    public SolicitudOutputDTO aprobar(@PathVariable Long id) {
        return solicitudesService.aprobarSolicitud(id);
    }

    @PostMapping("/{id}/denegar")
    public SolicitudOutputDTO denegar(@PathVariable Long id) {
        return solicitudesService.denegarSolicitud(id);
    }
}
