package Handlers;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorDescripcion;
import AdministracionDeHechos.CriterioPertenencia.PorFechaCarga;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class GetColeccionesHandler implements Handler {

    @Override
    public void handle(Context ctx) {

        ctx.result("Estos son los hechos del sistema");
    }
}
