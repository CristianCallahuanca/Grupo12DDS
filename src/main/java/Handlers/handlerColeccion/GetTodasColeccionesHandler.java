package Handlers.handlerColeccion;

import Infraestructura.Repositorios.ColeccionRepositorio;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class GetTodasColeccionesHandler implements Handler{

    @Override
    public void handle(Context ctx) {

        ctx.json(ColeccionRepositorio.getInstancia().obtenerTodas());
    }
}
