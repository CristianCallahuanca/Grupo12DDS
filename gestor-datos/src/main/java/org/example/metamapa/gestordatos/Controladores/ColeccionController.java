package org.example.metamapa.gestordatos.Controladores;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/gestordatos")
public class ColeccionController {

    private final IColeccionesService coleccionService;

    public ColeccionController(IColeccionesService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @PostMapping
    public ResponseEntity<ColeccionOutputDTO> crear(@RequestBody ColeccionInputDTO coleccion) {

        coleccionService.crearColeccion();

        return ResponseEntity.status(201).body();
    }

    @GetMapping
    public ResponseEntity<List<Coleccion>> listar() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{handle}")
    public ResponseEntity<Coleccion> actualizar(@PathVariable String handle,
                                                @RequestBody Coleccion datos) {
        return ResponseEntity.ok(service.update(handle, datos));
    }

    @DeleteMapping("/{handle}")
    public ResponseEntity<Void> eliminar(@PathVariable String handle) {
        service.delete(handle);
        return ResponseEntity.noContent().build();
    }
}
