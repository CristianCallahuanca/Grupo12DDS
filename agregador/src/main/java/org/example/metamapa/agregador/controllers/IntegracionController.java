package org.example.metamapa.agregador.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntegracionController {
    @GetMapping("/hechos")
    String hechosCrudos() { return "Recibe GET agregador! OK"; }
}
