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


    @PostMapping("/coleccion")
    public ResponseEntity<String> crear(@RequestBody ColeccionInputDTO coleccion) {

        coleccionService.crearColeccion(coleccion);

        return ResponseEntity.status(201).body("coleccion creada correctamente");
    }

    @GetMapping("/coleccion/{handle}")
    public ResponseEntity<String> retrieve(@PathVariable String handle) {
        ColeccionOutputDTO coleccionOutput = this.coleccionService.retrieveColeccion(handle);
        if (coleccionOutput == null) {
            return ResponseEntity.status(400).body("No se encontro la coleccion solicitada");
        }
        return ResponseEntity.status(200).body("coleccion encontrada correctamente");
    }

    @PutMapping("/coleccion/{handle}")
    public ResponseEntity<String> update(@PathVariable String handle, @RequestBody ColeccionInputDTO datos) {
        ColeccionOutputDTO coleccionOutput = this.coleccionService.updateColeccion(datos, handle);
        if (coleccionOutput == null) {
            return ResponseEntity.status(400).body("coleccion no actualizada, no se encontro la coleccion solicitada");
        }
        return ResponseEntity.status(200).body("coleccion actualizada correctamente");
    }

    @DeleteMapping("/coleccion/{handle}")
    public ResponseEntity<String> deleteColeccion(@PathVariable String handle) {
        boolean aux = this.coleccionService.eliminarColeccion(handle);
        if (!aux) {
            return ResponseEntity.status(400).body("coleccion no eliminada, no se encontro la coleccion solicitada");
        }
        return ResponseEntity.status(200).body("coleccion eliminada correctamente");
    }

    /*@GetMapping
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
    }*/
}
