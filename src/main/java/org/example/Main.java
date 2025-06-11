package org.example;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorDescripcion;
import AdministracionDeHechos.CriterioPertenencia.PorFechaCarga;
import AdministracionDeHechos.Hecho;
import Fuentes.FuenteDinamica;
import Fuentes.FuenteEstatica.Dataset;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Handlers.GetHechosHandler;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import Persona.Administrador;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Handlers.GetHechosHandler;
import Handlers.GetColeccionesHandler;
import Handlers.PostSolicitudesHandler;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        Javalin app = Javalin.create().start(7000);

        app.get("/hechos", new GetHechosHandler());
        app.get("/colecciones/<identificador>/hechos", new GetHechosHandler());
        /*app.get("/colecciones", new GetColeccionesHandler());*/
        app.post("/solicitudes",new PostSolicitudesHandler());



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

/*
LocalDateTime fa1 = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime fc1 = LocalDateTime.of(2025, 12, 1, 12, 15);

        FuenteDinamica fuentePrueba = new FuenteDinamica();

        PorFechaCarga criterioTiempoCarga = new PorFechaCarga(fa1, fc1);
        PorDescripcion criterioDescripcion = new PorDescripcion("generando gran preocupación entre los vecinos");

        List<CriterioDePertenencia> criterioPrueba = Arrays.asList(criterioTiempoCarga);

        Coleccion coleccionPrueba = new Coleccion(fuentePrueba, "Coleccion de prueba", "", criterioPrueba, "1");

        ColeccionRepositoryEnMemoria.getInstancia().guardar(coleccionPrueba);
*/

