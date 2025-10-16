package org.example.metamapa.gestordatos.controllers.administrativa;

import jakarta.validation.Valid;
import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.models.dtos.input.AlgoritmoConsensoInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestordatos/admin/colecciones")
public class ColeccionesAdminController {

    private final IColeccionesService coleccionService;

    public ColeccionesAdminController(IColeccionesService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @PostMapping
    public ResponseEntity<ColeccionOutputDTO> crear(@Valid @RequestBody ColeccionInputDTO dto) {
        coleccionService.crearColeccion(dto);
        return ResponseEntity.created(URI.create("/gestordatos/admin/colecciones"))
                .body(null);
    }

    @GetMapping
    public ResponseEntity<List<ColeccionOutputDTO>> listar() {
        return ResponseEntity.ok(coleccionService.listarColecciones());
    }

    @PutMapping("/{handle}")
    public ResponseEntity<ColeccionOutputDTO> actualizar(@PathVariable String handle,
                                                         @RequestBody Map<String, String> cambios) {
        boolean ok = coleccionService.actualizarColeccion(handle, cambios);
        if (!ok) return ResponseEntity.notFound().build();

        var dto = coleccionService.retrieveColeccion(handle);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{handle}")
    public ResponseEntity<Void> eliminar(@PathVariable String handle) {
        boolean eliminado = coleccionService.eliminarColeccion(handle);
        if (!eliminado) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{handle}/algoritmo")
    public ResponseEntity<ColeccionOutputDTO> cambiarAlgoritmo(
            @PathVariable String handle,
            @RequestBody AlgoritmoConsensoInputDTO body) {

        var result = coleccionService.updateAlgoritmo(handle, body.getAlgoritmo());
        if (result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }


    @PatchMapping("/{handle}/fuentes")
    public ResponseEntity<ColeccionOutputDTO> actualizarFuentes(@PathVariable String handle,
                                                                @RequestBody Map<String, List<Integer>> body) {
        var result = coleccionService.updateFuente(body.get("origenes"), handle);
        if (result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }
}
