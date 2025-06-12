package Handlers;


import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorTitulo;
import AdministracionDeHechos.Hecho;

import Fuentes.FuenteEstatica.FuenteEstatica;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;

import SolicitudEliminar.SolicitudEliminar;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        // FuenteEstatica.getInstancia().buscarPorTitulo(titulo); //
        // FuenteDinamica.getInstancia().buscarPorTitulo(titulo); //
        // TODO: Buscar tambirn en otras fuentes cuando estrn implementadas

         */

        if (titulo == null || justificacion == null) {
            ctx.status(400).result("Faltan campos obligatorios: título o justificación");
            return;
        }


        List<CriterioDePertenencia> listTitulo = new ArrayList<>();
        PorTitulo criterioTitulo = new PorTitulo(titulo);
        listTitulo.add(criterioTitulo);

        List<Hecho> hecho = HechoRepositoryEnMemoria.getInstancia().filtrarHechosDelSistema(listTitulo);


        if (hecho.isEmpty()){
            ctx.status(400).result("no se encontro el hecho");
            return;
        }

        SolicitudEliminar solicitud = new SolicitudEliminar(hecho.get(0),justificacion);

        ctx.status(200).result("llego con exito");

    }
}


