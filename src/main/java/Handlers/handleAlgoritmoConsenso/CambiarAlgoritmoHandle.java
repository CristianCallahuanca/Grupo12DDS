package Handlers.handleAlgoritmoConsenso;

import AdministracionDeHechos.Consenso.Absoluta;
import AdministracionDeHechos.Consenso.AlgoritmoDeConsenso;
import AdministracionDeHechos.Consenso.MayoriaSimple;
import AdministracionDeHechos.Consenso.MultiplesMenciones;
import Servicios.ServicioIdentificadorDeObjetos;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class CambiarAlgoritmoHandle implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        BodyAlgoritmo datos = ctx.bodyAsClass(BodyAlgoritmo.class);
        String handle = ctx.pathParam("handle");

        ServicioIdentificadorDeObjetos.getInstancia().obtenerColeccionPorHandle(handle).setAlgoritmoDeConsenso(JsonAlgoritmo(datos.getAlgoritmo()));

    }

    public AlgoritmoDeConsenso JsonAlgoritmo(String algoritmo) {

        return switch(algoritmo.toLowerCase()){
            case "absoluta" -> new Absoluta();

            case "mayoriasimple" -> new MayoriaSimple();

            case "multiplesmenciones" -> new MultiplesMenciones();

            default -> throw new IllegalArgumentException("Tipo de algoritmo no valido");
        };
    }
}
