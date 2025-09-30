package org.example.metamapa.gestordatos.Controladores;

import org.example.metamapa.gestordatos.Servicios.IContribuyenteService;
import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.SolicitudInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.SolicitudOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestordatos")
public class ContribuyenteController {

    private final IContribuyenteService contribuyenteService;

    ContribuyenteController(IContribuyenteService contribuyenteService){
        this.contribuyenteService = contribuyenteService;
    }

    @PostMapping("/registrarse")
    public ResponseEntity<String> crearUsuario(@RequestBody ContribuyenteRegInputDTO constribuyenteInputDTO){

        ContribuyenteRegistrado nuevoContribuyente = this.contribuyenteService.crearContribuyenteRegistrado(constribuyenteInputDTO);

        if(nuevoContribuyente == null){
            return ResponseEntity.status(400).body("ID del contribuyente no encontrado");
        }
        return ResponseEntity.status(201).body("contribuyente registrado correctamente");
    }

}
