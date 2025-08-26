package org.example.metamapa.proxy;

import org.example.metamapa.common.HechoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class HealthController {
    @GetMapping("/ping")
    String ping() { return "loader-proxy OK"; }
}

@RestController
class HechosController {
    @GetMapping("/hechos")
    List<HechoDTO> hechos() {
        return List.of(
                new HechoDTO("1","proxy","Derrame reportado"),
                new HechoDTO("2","proxy","Incendio en reserva")
        );
    }
}