package org.example.metamapa.gestordatos.controllers.publica;

import jakarta.validation.Valid;
import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/gestordatos/publica/colecciones")
public class ColeccionesPublicasController {

    private final IColeccionesService coleccionService;

    public ColeccionesPublicasController(IColeccionesService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/{handle}")
    public ResponseEntity<ColeccionOutputDTO> getColeccion(@PathVariable String handle) {
        ColeccionOutputDTO coleccion = coleccionService.retrieveColeccion(handle);
        if (coleccion == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(coleccion);
    }

    @GetMapping("/{handle}/hechos")
    public ResponseEntity<List<HechoOutputDTO>> getHechosPorCriterio(@PathVariable String handle,
                                                                     @RequestBody(required = false)
                                                                     @Valid List<CriterioRequest> criterios,
                                                                     @RequestParam(name = "page", required = false) Integer page,
                                                                     @RequestParam(name = "size", required = false) Integer size,
                                                                     @RequestParam(name = "sort", required = false) String sort) {
        // Por ahora ignoramos page/size/sort en el service; quedan listos en el contrato.
        List<CriterioRequest> safe = (criterios == null) ? Collections.emptyList() : criterios;
        List<HechoOutputDTO> hechos = coleccionService.retrieveHechosColeccion(handle, safe);
        if (hechos == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(hechos);
    }

    @GetMapping("/{handle}/modoNavegacion")
    public ResponseEntity<List<HechoOutputDTO>> navegarPorModo(@PathVariable String handle,
                                                               @RequestParam("modo") String modo) {
        List<HechoOutputDTO> hechos = coleccionService.retrieveColeccionModoNavegacion(handle, modo);
        if (hechos == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(hechos);
    }
}
