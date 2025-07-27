package Handlers.handlerColeccion;

import Infraestructura.Repositorios.ColeccionRepositorio;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class DeleteColeccionesHandler implements Handler {

    @Override
    public void handle(Context ctx) {

        ColeccionRepositorio.getInstancia().eliminarPorHandle(ctx.pathParam("handle"));
    }
}

