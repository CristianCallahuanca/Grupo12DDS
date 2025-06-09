package org.example;

import Persona.Administrador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Administrador pepe = new Administrador("pepe", "pepito", 11);

        logger.info("Se creó un administrador: {}");
        logger.warn("Advertencia de prueba: el administrador no tiene permisos.");
        logger.error("Error simulado para ver funcionamiento del logger.");
    }
}
