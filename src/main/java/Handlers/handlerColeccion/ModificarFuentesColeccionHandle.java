package Handlers.handlerColeccion;

import Handlers.ConversorStringObjetos;
import Servicios.ServicioIdentificadorDeObjetos;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import Fuentes.Fuente;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ModificarFuentesColeccionHandle implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        BodyModificarFuentesColeccion datos = ctx.bodyAsClass(BodyModificarFuentesColeccion.class);
        String handle = ctx.pathParam("handle");

        List<Fuente> fuentes = ServicioIdentificadorDeObjetos.getInstancia().obtenerColeccionPorHandle(handle).getFuentes();

        for(String fuenteDelete: datos.getFuentesABorrar()){
            //el problema es que tengo del json las fuentes como string pero quiero borrar un objeto cuando coincida el nombre de la clase
            //fuente.getClass().getSimpleName() => esto me da el nombre de la clase en string y lo comparo con el valor del json
            fuentes.removeIf(fuente -> fuente.getClass().getSimpleName().equalsIgnoreCase(fuenteDelete));
        }

        for(String fuenteAdd: datos.getNuevasFuentes()){

            fuentes.add(ConversorStringObjetos.JsonAFuente(fuenteAdd));
        }

        ServicioIdentificadorDeObjetos.getInstancia().obtenerColeccionPorHandle(handle).setFuentes(fuentes);
    }
}
