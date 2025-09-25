package org.example.metamapa.gestordatos.Controladores;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/gestordatos")
public class ColeccionController {

    private final IColeccionesService coleccionService;

    public ColeccionController(IColeccionesService coleccionService) {
        this.coleccionService = coleccionService;
    }


    @PostMapping("/colecciones")
    public ResponseEntity<String> crear(@RequestBody ColeccionInputDTO coleccion){

        coleccionService.crearColeccion(coleccion);

        return ResponseEntity.status(201).body("coleccion creada correctamente");
    }

    @GetMapping("/colecciones/{handle}")
    public ResponseEntity<ColeccionOutputDTO> retrieve(@PathVariable String handle) {
        ColeccionOutputDTO coleccionOutput = this.coleccionService.retrieveColeccion(handle);
        if (coleccionOutput == null) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(coleccionOutput);
    }

    @GetMapping("/colecciones/{handle}/hechos")

    public ResponseEntity<List<HechoOutputDTO>> retrieveHechosColeccion (@PathVariable String handle, @RequestBody List<CriterioRequest> criterios){

        List<HechoOutputDTO> hechos = this.coleccionService.retrieveHechosColeccion(handle,criterios);

        return ResponseEntity.status(200).body(hechos);
    }

    @GetMapping("/colecciones/{handle}/modoNavegacion") //es así o al revés??
    public ResponseEntity<List<HechoOutputDTO>> retrieveModoNavegacion(@PathVariable String handle, @RequestBody Map<String, String> request) {
        List<HechoOutputDTO> HechoOutputDTO = this.coleccionService.retrieveColeccionModoNavegacion(handle, request.get("modo_de_navegacion"));
        if (HechoOutputDTO == null) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(HechoOutputDTO);
    }

    @PatchMapping("/colecciones/algoritmo_de_consenso/{handle}")
    public ResponseEntity<ColeccionOutputDTO> updateAlgoritmo(@PathVariable String handle, @RequestBody Map<String, String> request) {
        ColeccionOutputDTO coleccionOutput = this.coleccionService.updateAlgoritmo(handle, request.get("algoritmo"));
        if (coleccionOutput == null) {
            return ResponseEntity.status(400).body(coleccionOutput);
        }
        return ResponseEntity.status(200).body(coleccionOutput);
    }

    @PatchMapping("/colecciones/origen/{handle}")
    public ResponseEntity<ColeccionOutputDTO> updateOrigen(@PathVariable String handle, @RequestBody Map<String, List<Integer>> request) {
        ColeccionOutputDTO coleccionOutput = this.coleccionService.updateFuente(request.get("origen"), handle);
        if (coleccionOutput == null) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(coleccionOutput);
    }

    @DeleteMapping("/colecciones/{handle}")
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
