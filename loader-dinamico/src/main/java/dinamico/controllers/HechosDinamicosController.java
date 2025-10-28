package dinamico.controllers;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO_OUT;
import dinamico.service.IHechosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("fuenteDinamica")
public class HechosDinamicosController {

    private final IHechosService hechosService;

    public HechosDinamicosController(IHechosService hechosService){
        this.hechosService = hechosService;
    }

    @PostMapping("/hecho")
    public ResponseEntity<String> subirHechoPublico(@Valid @RequestBody HechoCrudoDTO_IN hecho) {
        hechosService.cargarHecho(hecho);
        return ResponseEntity.status(HttpStatus.CREATED).body("Hecho cargado correctamente");
    }


    @GetMapping("/hechos")
    public ResponseEntity<List<HechoCrudoDTO_OUT>> obtenerHechos() {
        List<HechoCrudoDTO_OUT> hechos = hechosService.obtenerHechos();

        if (hechos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(hechos); // 200
    }


    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Loader Dinamico disponible");
    }

}




