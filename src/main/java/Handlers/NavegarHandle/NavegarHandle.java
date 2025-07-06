package Handlers.NavegarHandle;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.ModoNavegacion.Curada;
import AdministracionDeHechos.ModoNavegacion.ModoNavegacion;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class NavegarHandle implements Handler {

    @Override
    public void handle(Context ctx) {
        // Leer parametro del query params
        String handle = ctx.pathParam("handle");
        String modoNavegacion = ctx.queryParam("modo");

        Coleccion coleccion = ColeccionRepositoryEnMemoria.getInstancia().buscarPorHandle(handle);

        if(modoNavegacion.equalsIgnoreCase("curada")) {
           ctx.json(coleccion.obtenerHechosPorModo(new Curada()));
        }

        if(modoNavegacion.equalsIgnoreCase("irrestricta")){
            ctx.json(coleccion);
        }

    }




}
