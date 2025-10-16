package org.example.metamapa.gestordatos.controllers.publica;

import org.example.metamapa.gestordatos.Servicios.IContribuyenteService;
import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
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
    public ResponseEntity<String> registrar(@RequestBody ContribuyenteRegInputDTO inputDTO) {
        var contribuyente = contribuyenteService.crearContribuyenteRegistrado(inputDTO);
        if (contribuyente == null)
            return ResponseEntity.badRequest().body("Error en el registro");
        return ResponseEntity.status(201).body("Contribuyente registrado correctamente");
    }
}
