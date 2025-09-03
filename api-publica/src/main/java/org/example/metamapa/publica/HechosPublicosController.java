package org.example.metamapa.publica;

import org.example.metamapa.common.HechoDTOCommon;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
class HechosPublicosController {
    private final RestClient agregador;
    HechosPublicosController(RestClient agregadorClient){ this.agregador = agregadorClient; }

    @GetMapping("/hechos")
    List<HechoDTOCommon> listar(){
        return agregador.get().uri("/hechos-aggregados")
                .retrieve().body(new ParameterizedTypeReference<>() {});
    }
}
