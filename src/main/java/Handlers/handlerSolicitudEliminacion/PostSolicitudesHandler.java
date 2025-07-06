package Handlers.handlerSolicitudEliminacion;


import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorTitulo;
import AdministracionDeHechos.Hecho;

import Handlers.handlerHechos.BodyMessage;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;

import Infraestructura.Repositorios.SolicitudRepositoryEnMemoria;
import Servicios.ServicioFiltradorDeHechos;
import SolicitudEliminar.SolicitudEliminar;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostSolicitudesHandler implements Handler{

    @Override
    public void handle(@NotNull Context ctx) throws Exception{

        BodyMessage datos = ctx.bodyAsClass(BodyMessage.class);
        String titulo = null;
        String justificacion = null;

        titulo = datos.getTitulo();
        justificacion = datos.getJustificacion();

        if (titulo == null || justificacion == null) {
            ctx.status(400).result("Faltan campos obligatorios: título o justificación");
            return;
        }

        List<CriterioDePertenencia> listTitulo = new ArrayList<>();
        PorTitulo criterioTitulo = new PorTitulo(titulo);
        listTitulo.add(criterioTitulo);

        List<Hecho> hechos = ServicioFiltradorDeHechos.filtrarHechos(HechoRepositoryEnMemoria.getInstancia().obtenerTodosLosHechosDelSistema(), listTitulo);

        if (hechos.isEmpty()){
            ctx.status(400).result("no se encontro el hecho");
            return;
        }

        SolicitudEliminar solicitud = new SolicitudEliminar(hechos.get(0),justificacion);

        SolicitudRepositoryEnMemoria.getInstancia().guardar(solicitud);

        ctx.status(200).result("llego con exito");

    }
}


