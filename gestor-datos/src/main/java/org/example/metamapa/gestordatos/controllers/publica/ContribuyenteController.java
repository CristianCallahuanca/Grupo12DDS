package org.example.metamapa.gestordatos.controllers.publica;

import org.example.metamapa.gestordatos.Servicios.IContribuyenteService;
import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestordatos/contribuyentes")
public class ContribuyenteController {

    private final IContribuyenteService contribuyenteService;

    public ContribuyenteController(IContribuyenteService contribuyenteService) {
        this.contribuyenteService = contribuyenteService;
    }

    @PostMapping("/registrarse")
    public ResponseEntity<?> registrar(@RequestBody ContribuyenteRegInputDTO inputDTO) {
        var usuario = contribuyenteService.crearContribuyenteRegistrado(inputDTO);

        AuthResponse response = new AuthResponse(
                null, // Por ahora sin JWT
                "Usuario registrado correctamente",
                usuario.getUserId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRol()
        );

        return ResponseEntity.status(201).body(response);
    }
}

/*
*
*   private String nombre;
    private String apellido;
    private Integer dni;
    private Date fechaNacimiento;
    private String email;
    private String password;*/
