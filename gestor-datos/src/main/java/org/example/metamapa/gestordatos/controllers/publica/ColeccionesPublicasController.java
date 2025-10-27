package org.example.metamapa.gestordatos.controllers.publica;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestordatos/publica/colecciones")
@RequiredArgsConstructor
public class ColeccionesPublicasController {

    private final IColeccionesService coleccionService;

    @GetMapping
    public ResponseEntity<?> obtenerColecciones() {
        List<ColeccionOutputDTO> coleccionesOutput = this.coleccionService.retrieveColecciones();
        return ResponseEntity.ok(Map.of("estado", "ok", "colecciones", coleccionesOutput));
    }

    @GetMapping("/{handle}")
    public ResponseEntity<?> getColeccion(@PathVariable String handle) {
        ColeccionOutputDTO coleccion = coleccionService.retrieveColeccion(handle);
        if (coleccion == null)
            return ResponseEntity.status(404)
                    .body(Map.of("mensaje", "Colección no encontrada", "estado", "error"));
        return ResponseEntity.ok(Map.of("estado", "ok", "coleccion", coleccion));
    }

    @GetMapping("/{handle}/hechos")
    public ResponseEntity<?> getHechosPorCriterio(@PathVariable String handle,
                                                  @RequestParam Map<String, String> queryParams) {

        List<HechoOutputDTO> hechos = coleccionService.retrieveHechosColeccion(handle, queryParams);

        if (hechos == null)
            return ResponseEntity.status(404)
                    .body(Map.of("mensaje", "Colección no encontrada", "estado", "error"));

        return ResponseEntity.ok(Map.of("estado", "ok", "hechos", hechos));
    }




    @GetMapping("/{handle}/modoNavegacion")
    public ResponseEntity<?> navegarPorModo(@PathVariable String handle,
                                            @RequestParam Map<String, String> queryParams) {

        List<HechoOutputDTO> hechos = coleccionService.retrieveColeccionModoNavegacion(handle, queryParams);

        if (hechos == null)
            return ResponseEntity.status(404)
                    .body(Map.of("mensaje", "Colección no encontrada", "estado", "error"));

        String modo = queryParams.getOrDefault("modo", "curada");

        return ResponseEntity.ok(Map.of(
                "estado", "ok",
                "modo", modo,
                "hechos", hechos
        ));
    }

}
