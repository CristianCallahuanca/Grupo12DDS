package org.example.metamapa.gestordatos.controllers.publica;

import org.example.metamapa.gestordatos.Servicios.IContribuyenteService;
import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.DarRolAdminRequest;
import org.example.metamapa.gestordatos.models.dtos.input.LoginRequest;
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
        var response = contribuyenteService.crearContribuyenteRegistrado(inputDTO);

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = contribuyenteService.login(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/darRolAdmin")
    public ResponseEntity<String> rolAdmin(@RequestBody DarRolAdminRequest request) {
        Boolean seAsignoRol = contribuyenteService.rolAdminService(request.getEmail(), request.getPassword());

        if(seAsignoRol) {
            return ResponseEntity.status(200).body("Se le asignó el rol ADMIN");
        }
        return ResponseEntity.status(400).body("No se le pudo asignar el rol");
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
