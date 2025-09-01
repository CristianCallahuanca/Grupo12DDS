package org.example.metamapa.publica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.example.metamapa.publica.clientes")
public class ApiPublicaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiPublicaApplication.class, args);
    }
}
