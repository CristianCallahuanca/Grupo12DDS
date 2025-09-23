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
    public ResponseEntity<String> create(@RequestBody SolicitudInputDTO solicitudDTO) {
        SolicitudOutputDTO solicitud = this.solicitudesService.crearSolicitudEliminacion(solicitudDTO);
        if(solicitud == null){
            return ResponseEntity.status(400).body("ID del hecho no encontrado");
        }
        return ResponseEntity.status(201).body("solicitud creada correctamente");
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<String> aprobar(@PathVariable Long id) {
        SolicitudOutputDTO solicitud = this.solicitudesService.aprobarSolicitud(id);
        if(solictud == null){
            return ResponseEntity.status(400).body("ID del hecho no encontrado");
        }
        return ResponseEntity.status(200).body("solicitud aprobada correctamente");

    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<String> rechazar(@PathVariable Long id) {
        SolicitudOutputDTO solicitud = solicitudesService.denegarSolicitud(id);
        if(solicitud == null){
            return ResponseEntity.status(400).body("ID del hecho no encontrado");
        }

        return ResponseEntity.status(200).body("solicitud rechazada correctamente");
    }
}
