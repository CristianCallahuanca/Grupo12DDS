package org.example.metamapa.gestordatos.controllers.administrativa;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IOrigenRealService;
import org.example.metamapa.gestordatos.models.dtos.input.AlgoritmoConsensoInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.OrigenRealDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestordatos/admin/colecciones")
@RequiredArgsConstructor
public class ColeccionesAdminController {

    private final IColeccionesService coleccionService;
    private final IOrigenRealService origenRealService;

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ColeccionInputDTO dto) {
        ColeccionOutputDTO nueva = coleccionService.crearColeccion(dto);
        if (nueva == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "No se pudo crear la colección", "estado", "error"));
        }
        return ResponseEntity.created(URI.create("/gestordatos/admin/colecciones/" + nueva.getHandle()))
                .body(Map.of("mensaje", "Colección creada correctamente", "estado", "ok", "coleccion", nueva));
    }

    @GetMapping
    public ResponseEntity<List<ColeccionOutputDTO>> listar() {
        return ResponseEntity.ok(coleccionService.listarColecciones());
    }

    @PutMapping("/{handle}")
    public ResponseEntity<?> actualizar(@PathVariable String handle,
                                        @Valid @RequestBody ColeccionInputDTO cambios) {

        if (cambios == null ) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "No se enviaron cambios para actualizar", "estado", "error"));
        }

        boolean ok = coleccionService.actualizarColeccion(handle, cambios);
        if (!ok)
            return ResponseEntity.notFound()
                    .build();

        var dto = coleccionService.retrieveColeccion(handle);
        return ResponseEntity.ok(Map.of("mensaje", "Colección actualizada correctamente", "estado", "ok", "coleccion", dto));
    }

    @DeleteMapping("/{handle}")
    public ResponseEntity<?> eliminar(@PathVariable String handle) {
        boolean eliminado = coleccionService.eliminarColeccion(handle);
        if (!eliminado)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(Map.of("mensaje", "Colección eliminada correctamente", "estado", "ok"));
    }

    @PatchMapping("/{handle}/algoritmo")
    public ResponseEntity<?> cambiarAlgoritmo(@PathVariable String handle,
                                              @RequestBody AlgoritmoConsensoInputDTO body) {

        if (body == null || body.getAlgoritmo() == null || body.getAlgoritmo().isBlank()) {
            return ResponseEntity.unprocessableEntity()
                    .body(Map.of("mensaje", "El algoritmo proporcionado no es válido", "estado", "error"));
        }

        var result = coleccionService.updateAlgoritmo(handle, body.getAlgoritmo());
        if (result == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(Map.of("mensaje", "Algoritmo actualizado correctamente", "estado", "ok", "coleccion", result));
    }

    @PatchMapping("/{handle}/origenes-reales")
    public ResponseEntity<?> actualizarOrigenesReales(
            @PathVariable String handle,
            @RequestBody Map<String, List<String>> body) {

        if (body == null || !body.containsKey("origenes")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "Debe incluir la lista de orígenes reales a actualizar", "estado", "error"));
        }

        List<String> nuevosOrigenes = body.get("origenes");
        var result = coleccionService.actualizarOrigenesReales(handle, nuevosOrigenes);

        if (result == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(Map.of(
                "mensaje", "Orígenes reales actualizados correctamente",
                "estado", "ok",
                "coleccion", result
        ));
    }


    @GetMapping("/origenes-reales")
    public ResponseEntity<List<OrigenRealDTO>> listarOrigenesReales() {
        return ResponseEntity.ok(origenRealService.listarTodos());
    }
}
