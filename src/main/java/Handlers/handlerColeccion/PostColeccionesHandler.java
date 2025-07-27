package Handlers.handlerColeccion;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import Fuentes.Fuente;
import Handlers.ConversorStringObjetos;
import Infraestructura.Repositorios.ColeccionRepositorio;
import io.javalin.http.Context;
import io.javalin.http.Handler; // ¡ESTE import es clave!
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostColeccionesHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        BodyColeccion datos = ctx.bodyAsClass(BodyColeccion.class);

        List<CriterioDePertenencia> listCriterios = new ArrayList<CriterioDePertenencia>();
        List<Fuente> listFuentes = new ArrayList<Fuente>();

        for(CriterioDTO criterio : datos.getCriterios()){

            listCriterios.add(ConversorStringObjetos.JsonACriterio(criterio.tipo,criterio.parametros));
        }

        for(String fuente: datos.getFuentes()){

            listFuentes.add(ConversorStringObjetos.JsonAFuente(fuente));
        }

        Coleccion coleccion = new Coleccion(listFuentes,datos.getTitulo(),datos.getDescripcion(),listCriterios);

        ColeccionRepositorio.getInstancia().guardar(coleccion);

        System.out.println("se creo la coleccion correctamente");

        System.out.println("tamanio de la lista:");

        System.out.println(listCriterios.size());

    }

}