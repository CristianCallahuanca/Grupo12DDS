package Handlers.handlerColeccion;

import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class GetTodasColeccionesHandler implements Handler{

    @Override
    public void handle(Context ctx) {

        ctx.json(ColeccionRepositoryEnMemoria.getInstancia().obtenerTodas());
    }
}
