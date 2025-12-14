package org.example.metamapa.gestordatos.controllers.administrativa;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestordatos/admin/hechos")
@RequiredArgsConstructor
public class HechosAdminController {

    private final IHechoService hechosService;

    @PostMapping("/aceptar")
    public ResponseEntity<HechoOutputDTO> aprobarHecho(@RequestParam Long id){
        return ResponseEntity.status(200).body(hechosService.aprobarSolicitud(id));
    }

    @PostMapping("/aceptarSugerencia")
    public ResponseEntity<HechoOutputDTO> aprobarHechoSugerencia(@RequestParam Long id, @RequestParam String justificacion){
        return ResponseEntity.status(200).body(hechosService.aprobarSugerenciaSolicitud(id,justificacion));
    }

    @PostMapping("/rechazar")
    public ResponseEntity<HechoOutputDTO> rechazarHecho(@RequestParam Long id){
        return ResponseEntity.status(200).body(hechosService.denegarSolicitud(id));
    }


}
