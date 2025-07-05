package Handlers.handleAprobarDesaprobarSolicitudHandle;

import Handlers.handlerColeccion.BodyColeccion;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AprobarDesaprobarSolicitudHandle implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        BodyAprobarDesaprobarSolicitudHandle datos = ctx.bodyAsClass(BodyAprobarDesaprobarSolicitudHandle.class);


    }

}
