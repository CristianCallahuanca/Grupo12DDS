package org.example.metamapa.gestordatos.controllers.administrativa;

import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestordatos/admin/solicitudes")
public class SolicitudesAdminController {

    private final ISolicitudesService solicitudesService;

    public SolicitudesAdminController(ISolicitudesService solicitudesService) {
        this.solicitudesService = solicitudesService;
    }

    @GetMapping
    public ResponseEntity<?> listarPendientes() {
        return ResponseEntity.ok(solicitudesService.listarSolicitudesPendientes());
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<String> aprobar(@PathVariable Long id) {
        var solicitud = solicitudesService.aprobarSolicitud(id);
        if (solicitud == null) return ResponseEntity.badRequest().body("Solicitud no encontrada");
        return ResponseEntity.ok("Solicitud aprobada correctamente");
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<String> rechazar(@PathVariable Long id) {
        var solicitud = solicitudesService.denegarSolicitud(id);
        if (solicitud == null) return ResponseEntity.badRequest().body("Solicitud no encontrada");
        return ResponseEntity.ok("Solicitud rechazada correctamente");
    }
}
