package dinamico.controllers;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO_OUT;
import dinamico.service.IHechosService;
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

    @PostMapping("/reportarhecho")
    public ResponseEntity<String> subirHechoPublico(@RequestBody HechoCrudoDTO_IN hecho) {
        hechosService.cargarHecho(hecho);

        return ResponseEntity.ok("El hecho se subio con exito");
    }

    @GetMapping("/hechos")
    public List<HechoCrudoDTO_OUT> obtenerHechos(){

        List<HechoCrudoDTO_OUT> hechos = hechosService.obtenerHechos();

        return hechos.stream().peek( e -> e.setOrigen("DINAMICA")).toList();

    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Loader Dinamico disponible");
    }

}




