package Handlers;

import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class DeleteColeccionesHandler implements Handler {

    @Override
    public void handle(Context ctx) {

        ColeccionRepositoryEnMemoria.getInstancia().eliminarPorHandle(ctx.pathParam("handle"));
    }
}

