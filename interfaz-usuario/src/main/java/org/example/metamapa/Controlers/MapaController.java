package org.example.metamapa.Controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapaController {

    @GetMapping("/mapa")
    public String mostrarMapa() {
        return "mapa"; // Esto mostrará el archivo mapa.html
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/mapa";
    }
}
