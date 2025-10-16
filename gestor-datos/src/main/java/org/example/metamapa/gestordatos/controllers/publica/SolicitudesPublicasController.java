package org.example.metamapa.gestordatos.controllers.publica;

import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestordatos/publica/solicitudes")
public class SolicitudesPublicasController {

    private final ISolicitudesService solicitudesService;

    public SolicitudesPublicasController(ISolicitudesService solicitudesService) {
        this.solicitudesService = solicitudesService;
    }

    @PostMapping
    public ResponseEntity<String> crearSolicitud(@RequestBody SolicitudInputDTO solicitudDTO) {
        var solicitud = solicitudesService.crearSolicitudEliminacion(solicitudDTO);
        if (solicitud == null) {
            return ResponseEntity.badRequest().body("Hecho no encontrado");
        }
        return ResponseEntity.status(201).body("Solicitud de eliminación creada correctamente");
    }
}
