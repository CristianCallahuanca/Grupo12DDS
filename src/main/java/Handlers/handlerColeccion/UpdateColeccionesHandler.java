package Handlers.handlerColeccion;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Fuente;
import Fuentes.FuenteDinamica;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Fuentes.Proxy.FuenteDemo;
import Fuentes.Proxy.FuenteMetaMapa;
import Handlers.ConversorStringObjetos;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateColeccionesHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        String handle = ctx.pathParam("handle");
        BodyColeccion datos = ctx.bodyAsClass(BodyColeccion.class);
        List<Fuente> listFuentes = new ArrayList<Fuente>();

        Coleccion coleccion = ColeccionRepositoryEnMemoria.getInstancia().buscarPorHandle(handle);

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
