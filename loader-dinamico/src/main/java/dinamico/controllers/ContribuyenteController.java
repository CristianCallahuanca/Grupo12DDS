package dinamico.controllers;


import dinamico.service.ICrearUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fuenteDinamica")
public class ContribuyenteController {

    private final ICrearUsuarioService usuarioService;

    public ContribuyenteController(ICrearUsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crearUsuario")
    public ResponseEntity<String> crearUsuario(@RequestBody usuarioDTO usuario){

        usuarioService.cargarUsuario(usuario);
        return ResponseEntity.ok("Se creo el usuario con exito");
    }
}
