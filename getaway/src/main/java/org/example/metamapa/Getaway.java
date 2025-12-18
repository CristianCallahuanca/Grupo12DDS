package org.example.metamapa;

import org.example.metamapa.Config.GatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GatewayProperties.class)
public class Getaway {
    public static void main(String[] args) {

        SpringApplication.run(Getaway.class, args);
    }
}