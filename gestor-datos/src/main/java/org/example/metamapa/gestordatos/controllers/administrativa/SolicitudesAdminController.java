package org.example.metamapa.gestordatos.controllers.administrativa;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/gestordatos/admin/solicitudes")
@RequiredArgsConstructor
public class SolicitudesAdminController {

    private final ISolicitudesService solicitudesService;

    @GetMapping
    public ResponseEntity<?> listarPendientes() {
        var pendientes = solicitudesService.listarSolicitudesPendientes();
        return ResponseEntity.ok(Map.of("mensaje", "Solicitudes pendientes obtenidas correctamente", "estado", "ok", "solicitudes", pendientes));
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobar(@PathVariable Long id) {
        SolicitudOutputDTO solicitud = solicitudesService.aprobarSolicitud(id);
        if (solicitud == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(Map.of("mensaje", "Solicitud aprobada correctamente", "estado", "ok", "solicitud", solicitud));
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazar(@PathVariable Long id) {
        SolicitudOutputDTO solicitud = solicitudesService.denegarSolicitud(id);
        if (solicitud == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(Map.of("mensaje", "Solicitud rechazada correctamente", "estado", "ok", "solicitud", solicitud));
    }
}
