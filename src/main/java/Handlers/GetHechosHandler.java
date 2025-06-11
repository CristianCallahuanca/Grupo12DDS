package Handlers;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import Fuentes.FuenteDinamica;
import Fuentes.FuenteEstatica.Dataset;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class GetHechosHandler implements Handler {

    @Override
    public void handle(Context ctx) {

        // Leer query params
        String fechaReporteDesdeStr = ctx.queryParam("fecha_reporte_desde");
        String fechaReporteHastaStr = ctx.queryParam("fecha_reporte_hasta");
        String fechaAcontecimientoDesdeStr = ctx.queryParam("fecha_acontecimiento_desde");
        String fechaAcontecimientoHastaStr = ctx.queryParam("fecha_acontecimiento_hasta");
        String categoria = ctx.queryParam("categoria");
        String latitudParam = ctx.queryParam("latitud");
        String longitudParam = ctx.queryParam("longitud");

        final Ubicacion ubicacionFinal = (latitudParam != null && longitudParam != null)
                ? new Ubicacion(Double.parseDouble(latitudParam), Double.parseDouble(longitudParam))
                : null;

        // Parsear fechas si están presentes
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm");

        LocalDateTime fechaReporteDesde = fechaReporteDesdeStr != null ? LocalDateTime.parse(fechaReporteDesdeStr, formatter) : null;
        LocalDateTime fechaReporteHasta = fechaReporteHastaStr != null ? LocalDateTime.parse(fechaReporteHastaStr, formatter) : null;
        LocalDateTime fechaAcontecimientoDesde = fechaAcontecimientoDesdeStr != null ? LocalDateTime.parse(fechaAcontecimientoDesdeStr, formatter) : null;
        LocalDateTime fechaAcontecimientoHasta = fechaAcontecimientoHastaStr != null ? LocalDateTime.parse(fechaAcontecimientoHastaStr, formatter) : null;

        // Obtener y filtrar colecciones
        List<Coleccion> colecciones = ColeccionRepositoryEnMemoria.getInstancia().obtenerTodas();

        List<Hecho> hechos;

        //Falta hacer BIEN fechaReporteDesde en adelante
        List<Coleccion> filtradas = colecciones.stream()
                .filter(c -> categoria == null || !c.filtrarHechos(List.of(new PorCategoria(categoria))).isEmpty())
                .filter(c -> (latitudParam == null && longitudParam == null) || !c.filtrarHechos(List.of(new PorUbicacion(ubicacionFinal))).isEmpty())
                .filter(c -> (fechaReporteDesde == null && fechaReporteHasta == null) ||
                        !c.filtrarHechos(List.of(new PorFechaCarga(fechaReporteDesde, fechaReporteHasta))).isEmpty())
                .filter(c -> (fechaAcontecimientoDesde == null && fechaAcontecimientoHasta == null) ||
                        !c.filtrarHechos(List.of(new PorFechaAcontecimiento(fechaAcontecimientoDesde, fechaAcontecimientoHasta))).isEmpty())
                .toList();

        ctx.json(colecciones);
    }
}

//FuenteEstatica fuentePrueba = new FuenteEstatica(new Dataset("/home/utnso/IdeaProjects/Grupo12DDS/datos/desastres_naturales_argentina.csv"));

//List<Hecho> prueba = fuentePrueba.procesarHechosDesdeCSV();

/*
* */