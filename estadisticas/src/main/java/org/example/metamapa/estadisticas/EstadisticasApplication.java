package org.example.metamapa.estadisticas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@EnableScheduling
@SpringBootApplication
public class EstadisticasApplication {
    public static void main(String[] args) {
        SpringApplication.run(EstadisticasApplication.class, args);

    }
}
