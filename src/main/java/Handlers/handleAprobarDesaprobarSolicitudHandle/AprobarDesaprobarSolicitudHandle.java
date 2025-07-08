package Handlers.handleAprobarDesaprobarSolicitudHandle;

import Infraestructura.Repositorios.SolicitudRepositoryEnMemoria;
import Servicios.ServicioIdentificadorDeObjetos;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AprobarDesaprobarSolicitudHandle implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        BodyAprobarDesaprobarSolicitudHandle datos = ctx.bodyAsClass(BodyAprobarDesaprobarSolicitudHandle.class);
        int id = Integer.parseInt(ctx.pathParam("id"));

        if(datos.getEstado().equals("aceptar")) {
            ServicioIdentificadorDeObjetos.getInstancia().obtenerSolicitudEliminar(id).aceptar();
        }
        else{
            ServicioIdentificadorDeObjetos.getInstancia().obtenerSolicitudEliminar(id).rechazar();
        }

    }

}
