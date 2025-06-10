package org.example;

import AdministracionDeHechos.Hecho;
import Fuentes.FuenteEstatica.Dataset;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Handlers.GetHechosHandler;
import Persona.Administrador;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Handlers.GetHechosHandler;
public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        Javalin app = Javalin.create().start(7000);

        // Definir ruta GET "/ping"
        app.get("/ping", ctx -> {
            ctx.result("Pong");
        });

        /*app.get("/hechos",  new GetHechosHandler());*/

        /*app.get("/colecciones/:identificador/hechos", new GetColeccionesHandler());
        app.post("/solicitudes",new PostSolicitudesHandler());*/

    }
}

/*Administrador pepe = new Administrador("pepe", "pepito", 11);

        logger.info("Se creó un administrador: {}");
        //logger.warn("Advertencia de prueba: el administrador no tiene permisos.");
        //logger.error("Error simulado para ver funcionamiento del logger.");

        Dataset dataset = new Dataset("/home/utnso/IdeaProjects/Grupo12DDS/datos/desastres_naturales_argentina.csv");

        FuenteEstatica fuenteXD = new FuenteEstatica(dataset);

        List<Hecho> hechos = new ArrayList<Hecho>();

        hechos = fuenteXD.procesarHechosDesdeCSV();

        System.out.println("se leyeron:");
        System.out.println(hechos.size());*/


