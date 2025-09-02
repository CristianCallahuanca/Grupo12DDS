package org.example.metamapa.agregador;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
class AgregadorConfig {

    @Bean RestClient loaderEstatico() { return RestClient.builder().baseUrl("http://localhost:8101/fuenteEstatica").build(); }
    @Bean RestClient loaderDinamico() { return RestClient.builder().baseUrl("http://localhost:8102/fuenteDinamica").build(); }
    @Bean RestClient loaderProxy()    { return RestClient.builder().baseUrl("http://localhost:8103/fuenteProxy").build(); }
}