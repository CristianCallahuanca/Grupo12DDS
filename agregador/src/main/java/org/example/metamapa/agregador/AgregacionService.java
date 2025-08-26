package org.example.metamapa.agregador;


import org.example.metamapa.common.HechoDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
class AgregacionService {
    private final RestClient estatico, dinamico, proxy;
    AgregacionService(RestClient loaderEstatico, RestClient loaderDinamico, RestClient loaderProxy) {
        this.estatico = loaderEstatico; this.dinamico = loaderDinamico; this.proxy = loaderProxy;
    }

    List<HechoDTO> agregar() {
        var all = new ArrayList<HechoDTO>();
        all.addAll(listar(estatico));
        all.addAll(listar(dinamico));
        all.addAll(listar(proxy));
        return all;
    }

    private List<HechoDTO> listar(RestClient c) {
        return c.get().uri("/hechos")
                .retrieve().body(new ParameterizedTypeReference<List<HechoDTO>>() {});
    }
}