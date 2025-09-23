package org.example.metamapa.gestordatos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestorDatosApplication {
    public static void main(String[] args) {
        SpringApplication.run(GestorDatosApplication.class, args);
    }
}
