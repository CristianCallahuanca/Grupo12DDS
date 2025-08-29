package org.example.metamapa.dinamico;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.metamapa.common.HechoDTO;

import java.util.List;

@RestController
class HealthController {
    @GetMapping("/ping")
    String ping() { return "loader-dinamico OK"; }
}

@RestController
class HechosController {
    @GetMapping("/hechos")
    List<HechoDTO> hechos() {
        return List.of(
                new HechoDTO("1","dinamico","Derrame reportado"),
                new HechoDTO("2","dinamico","Incendio en reserva")
        );
    }
}