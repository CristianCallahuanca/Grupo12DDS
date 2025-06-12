package Handlers;


import AdministracionDeHechos.Hecho;

import Fuentes.FuenteEstatica.FuenteEstatica;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;

import SolicitudEliminar.SolicitudEliminar;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class PostSolicitudesHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception{

        BodyMessage datos = ctx.bodyAsClass(BodyMessage.class);
        String titulo = null;
        String justificacion = null;

        titulo = datos.getTitulo();
        justificacion = datos.getJustificacion();

        /*IMPORTANTE HAY QUE BUSCAR EN LAS DEMAS FUENTES CUANDO ESTEN CREADAS, NECESITO QUE TODAS LAS FUENTES
        TENGAN BUSCAR POR TITULO
         */

        if (titulo == null || justificacion == null) {
            ctx.status(400).result("Faltan campos obligatorios: título o justificación");
            return;
        }

        Hecho hecho = null;

        hecho = HechoRepositoryEnMemoria.getInstancia().buscarPorTitulo(titulo);


        if(hecho == null){
            ctx.status(400).result("no se encontro el hecho");
            return;
        }

        SolicitudEliminar solicitud = new SolicitudEliminar(hecho,justificacion);

        ctx.status(200).result("llego con exito");

    }
}


