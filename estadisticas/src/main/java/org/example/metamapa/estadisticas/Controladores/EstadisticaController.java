package org.example.metamapa.estadisticas.Controladores;

import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gestordatos")
public class EstadisticaController {

        private final IEstadisticaService estadisticaService;

        public EstadisticaController(IEstadisticaService estadisticaService) {
            this.estadisticaService = estadisticaService;
        }

}
