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
import io.javalin.http.Handler; // ¡ESTE import es clave!
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        ColeccionRepositoryEnMemoria.getInstancia().guardar(coleccion);

        System.out.println("se creo la coleccion correctamente");

        System.out.println("tamanio de la lista:");

        System.out.println(listCriterios.size());

    }

}