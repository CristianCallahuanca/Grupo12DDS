package org.example.metamapa.agregador;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class AgregadorConfig {
    @Bean RestClient loaderEstatico() { return RestClient.builder().baseUrl("http://localhost:8101").build(); }
    @Bean RestClient loaderDinamico() { return RestClient.builder().baseUrl("http://localhost:8102").build(); }
    @Bean RestClient loaderProxy()    { return RestClient.builder().baseUrl("http://localhost:8103").build(); }
}