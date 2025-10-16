package org.example.metamapa.gestordatos.controllers.publica;

import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/gestordatos/publica/hechos")
public class HechosPublicosController {

    private final IHechoService hechosService;

    public HechosPublicosController(IHechoService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping
    public ResponseEntity<List<HechoOutputDTO>> obtenerHechosFiltrados(
            @RequestParam Map<String, String> queryParams) {

        List<CriterioRequest> criterios = hechosService.convertirQueryParamsACriterios(queryParams);
        return ResponseEntity.ok(hechosService.buscarTodosLosHechos(criterios));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editarHecho(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        boolean actualizado = hechosService.editarHechoContribuyente(id, cambios);
        if (!actualizado)
            return ResponseEntity.status(400).body("No se pudo actualizar el hecho (plazo vencido o no autorizado)");
        return ResponseEntity.ok("Hecho actualizado correctamente");
    }

}
