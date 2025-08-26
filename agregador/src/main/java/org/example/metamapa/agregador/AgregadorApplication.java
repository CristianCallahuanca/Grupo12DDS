package org.example.metamapa.agregador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // si vas a programar tareas periódicas
public class AgregadorApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgregadorApplication.class, args);
    }
}
