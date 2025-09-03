package org.example.metamapa;

import org.example.metamapa.common.HechoDTOCommon;
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
    List<HechoDTOCommon> hechos() {
        return List.of(
                new HechoDTOCommon("1","proxy","Derrame reportado"),
                new HechoDTOCommon("2","proxy","Incendio en reserva")
        );
    }
}