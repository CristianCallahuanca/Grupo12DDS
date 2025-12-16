package org.example.metamapa.gestordatos.controllers.publica;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestordatos/publica/hechos")
@RequiredArgsConstructor
public class HechosPublicosController {

    private final IHechoService hechosService;

    @GetMapping
    public ResponseEntity<List<HechoOutputDTO>> obtenerHechosFiltrados(
            @RequestParam Map<String, String> queryParams) {

        List<CriterioRequest> criterios =
                StringAObjetos.convertirQueryParamsACriterios(queryParams);

        List<HechoOutputDTO> hechos =
                hechosService.buscarTodosLosHechos(criterios);

        if (hechos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(hechos); // 200 + JSON array
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editarHecho(@PathVariable Long id, @RequestBody Map<String, Object> cambios) {
        boolean actualizado = hechosService.editarHechoContribuyente(id, cambios);
        if (!actualizado)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "No autorizado o plazo vencido", "estado", "error"));

        return ResponseEntity.ok(Map.of("mensaje", "Hecho actualizado correctamente", "estado", "ok"));
    }
}
