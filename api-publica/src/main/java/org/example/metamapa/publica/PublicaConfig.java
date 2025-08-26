package org.example.metamapa.publica;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class PublicaConfig {
    @Bean
    RestClient agregadorClient() {
        return RestClient.builder().baseUrl("http://localhost:8200").build();
    }
}
