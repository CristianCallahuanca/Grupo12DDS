package Handlers.handlerSolicitudEliminacion;


import AdministracionDeHechos.Hecho;

import Infraestructura.Repositorios.SolicitudRepositoryEnMemoria;
import Servicios.ServicioIdentificadorDeObjetos;
import SolicitudEliminar.SolicitudEliminar;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class PostSolicitudesHandler implements Handler{

    @Override
    public void handle(@NotNull Context ctx) throws Exception{

        BodyEliminarSolicitud datos = ctx.bodyAsClass(BodyEliminarSolicitud.class);
        String id_hecho = null;
        String justificacion = null;

        id_hecho = datos.getId_hecho();
        justificacion = datos.getJustificacion();

        if (id_hecho == null || justificacion == null) {
            ctx.status(400).result("Faltan campos obligatorios: título o justificación");
            return;
        }

        Hecho hecho = ServicioIdentificadorDeObjetos.getInstancia().obtenerHechoPorID(Integer.parseInt(id_hecho));

        if (hecho == null){
            ctx.status(400).result("no se encontro el hecho");
            return;
        }

        SolicitudEliminar solicitud = new SolicitudEliminar(hecho,justificacion);

        SolicitudRepositoryEnMemoria.getInstancia().guardar(solicitud);

        ctx.status(200).result("llego con exito");

    }
}


