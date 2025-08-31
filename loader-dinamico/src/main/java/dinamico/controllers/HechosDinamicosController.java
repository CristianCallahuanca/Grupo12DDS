package dinamico.controllers;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.service.ICargarHechosService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("fuenteDinamica")
public class HechosDinamicosController {

    private final ICargarHechosService hechosService;

    public HechosDinamicosController(ICargarHechosService hechosService){
        this.hechosService = hechosService;
    }

    @PostMapping("/reportarhecho")
    public ResponseEntity<String> subirHechoPublico(@RequestBody HechoCrudoDTO_IN hecho) {
        hechosService.cargarHecho(hecho);
        return ResponseEntity.ok("El hecho se subio con exito");
    }

}