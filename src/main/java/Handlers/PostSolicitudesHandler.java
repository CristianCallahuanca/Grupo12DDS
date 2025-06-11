package Handlers;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorDescripcion;
import AdministracionDeHechos.CriterioPertenencia.PorFechaCarga;
import AdministracionDeHechos.Ubicacion;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import Infraestructura.Repositorios.SolicitudRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class PostSolicitudesHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) {

        BodyMessage datos = ctx.bodyAsClass(BodyMessage.class);
        String titulo = datos.GetTituloHecho();
        String justificacion = datos.GetJustificacion();

        List<Coleccion> colecciones = ColeccionRepositoryEnMemoria.getInstancia().obtenerTodas();

        ctx.status(200).result("llego con exito")
        ;
        Ubicacion ubicacion = new Ubicacion(12,2);

        ctx.json(ubicacion);



        /*SolicitudRepositoryEnMemoria.getInstancia().guardar(this)*/


    }
}


