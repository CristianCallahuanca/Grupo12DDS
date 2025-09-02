package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.common.HechoDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgregacionService {
    private final RestClient estatico, dinamico, proxy;

    AgregacionService(RestClient loaderEstatico, RestClient loaderDinamico, RestClient loaderProxy) {
        this.estatico = loaderEstatico; this.dinamico = loaderDinamico; this.proxy = loaderProxy;
    }

    public List<HechoDTO> agregar() {
        List<HechoDTO> all = new ArrayList<>();
        all.addAll(listar(estatico));
        all.addAll(listar(dinamico));
        //all.addAll(listar(proxy));
        return all;
    }

    private List<HechoDTO> listar(RestClient c) {
        return c.get().uri("/hechos")
                .retrieve().body(new ParameterizedTypeReference<List<HechoDTO>>() {});
    }

    /*
    @Bean RestClient loaderEstatico() { return RestClient.builder().baseUrl("http://localhost:8101/fuenteEstatica").build(); }
    @Bean RestClient loaderDinamico() { return RestClient.builder().baseUrl("http://localhost:8102/fuenteDinamica").build(); }
    @Bean RestClient loaderProxy()    { return RestClient.builder().baseUrl("http://localhost:8103/fuenteProxy").build(); }
    * */
}