package org.example;

import Handlers.endpointsAPI;
import Handlers.handleAlgoritmoConsenso.CambiarAlgoritmoHandle;
import Handlers.handlerColeccion.DeleteColeccionesHandler;
import Handlers.handlerColeccion.GetTodasColeccionesHandler;
import Handlers.handlerColeccion.PostColeccionesHandler;
import Handlers.handlerColeccion.UpdateColeccionesHandler;
import Handlers.handlerHechos.GetHechosColeccionHandler;
import Handlers.handleAprobarDesaprobarSolicitudHandle.AprobarDesaprobarSolicitudHandle;
import Handlers.handlerColeccion.ModificarFuentesColeccionHandle;
import Handlers.NavegarHandle.NavegarHandle;
import Handlers.ReportarHechoHandle.ReportarHechoHandle;

import Handlers.handlerSolicitudEliminacion.PostSolicitudesHandler;
import Scheduler.Scheduler;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.*;

import Handlers.handlerHechos.GetHechosHandler;


public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        
        new endpointsAPI().iniciarEndpoints();

        new Scheduler().iniciarScheduler();

    }
}

/*Administrador pepe = new Administrador("pepe", "pepito", 11);

        logger.info("Se creó un administrador: {}");
        //logger.warn("Advertencia de prueba: el administrador no tiene permisos.");
        //logger.error("Error simulado para ver funcionamiento del logger.");

        Dataset dataset = new Dataset("/home/utnso/IdeaProjects/Grupo12DDS/datos/desastres_naturales_argentina.csv");

        FuenteEstatica fuenteXD = new FuenteEstatica(dataset);

        List<Hecho> hechos = new ArrayList<Hecho>();

        hechos = fuenteXD.obtenerHechos();

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

        ColeccionRepositorio.getInstancia().guardar(coleccionPrueba);
*/

