package Handlers;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorTitulo;
import AdministracionDeHechos.Hecho;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import SolicitudEliminar.SolicitudEliminar;
import io.javalin.http.Context;
import io.javalin.http.Handler; // ¡ESTE import es clave!
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostColeccionesHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {


    }
}