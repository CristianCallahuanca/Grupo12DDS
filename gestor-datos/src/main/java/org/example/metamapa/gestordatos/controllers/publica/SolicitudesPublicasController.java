package org.example.metamapa.gestordatos.controllers.publica;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.ISolicitudesService;
import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestordatos/publica/solicitudes")
@RequiredArgsConstructor
public class SolicitudesPublicasController {

    private final ISolicitudesService solicitudesService;

    @PostMapping
    public ResponseEntity<?> crearSolicitud(@RequestBody SolicitudInputDTO solicitudDTO) {
        SolicitudOutputDTO solicitud = solicitudesService.crearSolicitudEliminacion(solicitudDTO);
        if (solicitud == null)
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Hecho no encontrado", "estado", "error"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Solicitud creada correctamente", "estado", "ok", "solicitud", solicitud));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudOutputDTO>> obtenerSolicitudes(){

        return ResponseEntity.status(200).body(solicitudesService.listarSolicitudesPendientes());
    }

    @PostMapping("/aceptar")
    public ResponseEntity<SolicitudOutputDTO> aprobarSolicitud(@RequestParam Long id){
        return ResponseEntity.status(200).body(solicitudesService.aprobarSolicitud(id));
    }

    @PostMapping("/rechazar")
    public ResponseEntity<SolicitudOutputDTO> rechazarSolicitud(@RequestParam Long id){
        return ResponseEntity.status(200).body(solicitudesService.denegarSolicitud(id));
    }
}
