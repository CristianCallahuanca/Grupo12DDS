package org.example.metamapa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class LoaderMetamapaApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoaderMetamapaApplication.class, args);
        log.info("Loader Metamapa INICIADO");
    }
}
