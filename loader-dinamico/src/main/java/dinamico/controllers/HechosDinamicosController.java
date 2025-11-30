package dinamico.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO_OUT;
import dinamico.service.IFileUploadService;
import dinamico.service.IHechosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("fuenteDinamica")
public class HechosDinamicosController {

    private final IHechosService hechosService;

    public HechosDinamicosController(IHechosService hechosService){
        this.hechosService = hechosService;
    }

    @PostMapping(value = "/hecho", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirHechoPublico(
            @RequestPart("data") String data,
            @RequestPart("image") List<MultipartFile> files
    ) {

        // Convertimos el JSON manualmente
        ObjectMapper mapper = new ObjectMapper();
        HechoCrudoDTO_IN hecho;
        try {
            hecho = mapper.readValue(data, HechoCrudoDTO_IN.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON inválido: " + e.getMessage());
        }

        hechosService.cargarHecho(hecho, files);

        System.out.println("titulo = " + hecho.getTitulo());
        System.out.println("categoria = " + hecho.getCategoria());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Hecho cargado correctamente");
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




