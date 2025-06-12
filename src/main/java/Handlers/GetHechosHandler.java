package Handlers;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import AdministracionDeHechos.Ubicacion;
import AdministracionDeHechos.Hecho;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Fuentes.FuenteDinamica;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import static Handlers.GetColeccionesHandler.crearYAgregarSiNoNulo;

public class GetHechosHandler implements Handler {

    @Override
    public void handle(Context ctx) throws IOException {

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

        LocalDateTime fechaReporteDesde = fechaReporteDesdeStr != null ? LocalDateTime.parse(fechaReporteDesdeStr, formatter) : null;
        LocalDateTime fechaReporteHasta = fechaReporteHastaStr != null ? LocalDateTime.parse(fechaReporteHastaStr, formatter) : null;
        LocalDateTime fechaAcontecimientoDesde = fechaAcontecimientoDesdeStr != null ? LocalDateTime.parse(fechaAcontecimientoDesdeStr, formatter) : null;
        LocalDateTime fechaAcontecimientoHasta = fechaAcontecimientoHastaStr != null ? LocalDateTime.parse(fechaAcontecimientoHastaStr, formatter) : null;

        LocalDateTime fechaHardcodeadaHasta = LocalDateTime.of(4025, 12, 31, 23, 59);
        LocalDateTime fechaHardcodeadaDesde = LocalDateTime.of(2000, 1, 1, 1, 0);

        //Agregar criterios de filtraje si existen a la lista
        List<CriterioDePertenencia> criterios = new ArrayList<>();

        crearYAgregarSiNoNulo(categoria, PorCategoria::new, criterios);
        crearYAgregarSiNoNulo(ubicacionFinal, PorUbicacion::new, criterios);
        crearYAgregarSiNoNulo(fechaReporteDesde, fechaHardcodeadaHasta, PorFechaCarga::new, criterios);
        crearYAgregarSiNoNulo(fechaHardcodeadaDesde, fechaReporteHasta, PorFechaCarga::new, criterios);
        crearYAgregarSiNoNulo(fechaAcontecimientoDesde, fechaHardcodeadaHasta, PorFechaCarga::new, criterios);
        crearYAgregarSiNoNulo(fechaHardcodeadaDesde, fechaAcontecimientoHasta, PorFechaCarga::new, criterios);

        // Obtener y filtrar hechos del sistema
        List<Hecho>  hechosFiltradosDelSistema = Infraestructura.Repositorios.HechoRepositoryEnMemoria.getInstancia().filtrarHechosDelSistema(criterios);


        ctx.json(hechosFiltradosDelSistema);
    }
}