package Handlers.handlerColeccion;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import Fuentes.Fuente;
import Handlers.ConversorStringObjetos;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import Servicios.ServicioIdentificadorDeObjetos;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UpdateColeccionesHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        String handle = ctx.pathParam("handle");
        BodyColeccion datos = ctx.bodyAsClass(BodyColeccion.class);
        List<Fuente> listFuentes = new ArrayList<Fuente>();

        Coleccion coleccion = ServicioIdentificadorDeObjetos.getInstancia().obtenerColeccionPorHandle(handle);

        if (datos.getTitulo() != null) {
            coleccion.setTitulo(datos.getTitulo());
        }

        if (datos.getDescripcion() != null) {
            coleccion.setDescripcion(datos.getDescripcion());
        }

        if (datos.getFuentes() != null) {

            for(String fuente: datos.getFuentes()){

                listFuentes.add(ConversorStringObjetos.JsonAFuente(fuente));
            }

            coleccion.setFuentes(listFuentes);
        }

        if (datos.getCriterios() != null) {

            List<CriterioDePertenencia> nuevosCriterios = new ArrayList<CriterioDePertenencia>();

            for (CriterioDTO criterio : datos.getCriterios()) {

                nuevosCriterios.add(ConversorStringObjetos.JsonACriterio(criterio.tipo, criterio.parametros));
            }
            coleccion.setCriterios(nuevosCriterios);
        }

    }



}
