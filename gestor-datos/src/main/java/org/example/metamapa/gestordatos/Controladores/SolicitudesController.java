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
        return ResponseEntity.status(201).body("solicitud creada correctamente");
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<String> aprobar(@PathVariable Long id) {
        this.solicitudesService.aprobarSolicitud(id);
        return ResponseEntity.status(200).body("solicitud aprobada correctamente");

    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<String> rechazar(@PathVariable Long id) {
        solicitudesService.denegarSolicitud(id);
        return ResponseEntity.status(200).body("solicitud rechazada correctamente");
    }
}
