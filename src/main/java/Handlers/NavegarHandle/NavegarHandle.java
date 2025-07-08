package Handlers.NavegarHandle;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.ModoNavegacion.Curada;
import Servicios.ServicioIdentificadorDeObjetos;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.io.IOException;

public class NavegarHandle implements Handler {

    @Override
    public void handle(Context ctx) throws IOException {
        // Leer parametro del query params
        String handle = ctx.pathParam("handle");
        String modoNavegacion = ctx.queryParam("modo");

        Coleccion coleccion = ServicioIdentificadorDeObjetos.getInstancia().obtenerColeccionPorHandle(handle);

        if(modoNavegacion.equalsIgnoreCase("curada")) {
           ctx.json(coleccion.obtenerHechosPorModo(new Curada()));
        }

        if(modoNavegacion.equalsIgnoreCase("irrestricta")){
            ctx.json(coleccion);
        }

    }




}
