package Handlers.ReportarHechoHandle;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ReportarHechoHandle implements Handler {

    @Override
    public void handle(Context ctx) {
        // Leer parametro del query params
        BodyReportarHechoHandle datos = ctx.bodyAsClass(BodyReportarHechoHandle.class);



        ctx.json("");
    }
}

//titulo,desc,categoria,latitu,long,fechaAct,etiqueta,DNI

/*
*  String fechaReporteDesdeStr = ctx.queryParam("fecha_reporte_desde");
        String fechaReporteHastaStr = ctx.queryParam("fecha_reporte_hasta");
        String fechaAcontecimientoDesdeStr = ctx.queryParam("fecha_acontecimiento_desde");
        String fechaAcontecimientoHastaStr = ctx.queryParam("fecha_acontecimiento_hasta");
        String categoria = ctx.queryParam("categoria");
        String latitudParam = ctx.queryParam("latitud");
        String longitudParam = ctx.queryParam("longitud");
* */