package Handlers.ReportarHechoHandle;

import AdministracionDeHechos.EstadoHecho;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Proxy.FuenteMetaMapa;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.time.LocalDateTime;

public class ReportarHechoHandle implements Handler {

    @Override
    public void handle(Context ctx) {
        // Leer parametro del query params
        BodyReportarHechoHandle datos = ctx.bodyAsClass(BodyReportarHechoHandle.class);

        Ubicacion ubi = new Ubicacion(Double.parseDouble(datos.getLatitud()),Double.parseDouble(datos.getLongitud()));

        Hecho hecho = new Hecho(datos.getTitulo(), datos.getDescripcion(), datos.getCategoria(), ubi, LocalDateTime.parse(datos.getFechaAcontecimiento()), datos.getEtiqueta());
        hecho.setOrigen(Origen.PROXY);
        hecho.setFechaCarga(LocalDateTime.now());
        hecho.setFuente(FuenteMetaMapa.getInstancia());
        hecho.setEstadoHecho(EstadoHecho.EN_REVISION);

        FuenteMetaMapa.getInstancia().getHechos().add(hecho);
    }
}