package Handlers;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.*;
import AdministracionDeHechos.Ubicacion;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GetColeccionesHandler implements Handler {

    @Override
    public void handle(Context ctx) {
        // Leer parametro del query params
        String id = ctx.pathParam("identificador");

        // Leer query params
        String categoria = ctx.queryParam("categoria");
        String latitudParam = ctx.queryParam("latitud");
        String longitudParam = ctx.queryParam("longitud");
        String fechaReporteDesdeStr = ctx.queryParam("fecha_reporte_desde");
        String fechaReporteHastaStr = ctx.queryParam("fecha_reporte_hasta");
        String fechaAcontecimientoDesdeStr = ctx.queryParam("fecha_acontecimiento_desde");
        String fechaAcontecimientoHastaStr = ctx.queryParam("fecha_acontecimiento_hasta");

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

        // Creo lo Criterios de pertenencia respectivos si están presentes
        List<CriterioDePertenencia> criterios = new ArrayList<>();

        crearYAgregarSiNoNulo(categoria, PorCategoria::new, criterios);
        crearYAgregarSiNoNulo(ubicacionFinal, PorUbicacion::new, criterios);
        crearYAgregarSiNoNulo(fechaReporteDesde, fechaHardcodeadaHasta, PorFechaCarga::new, criterios);
        crearYAgregarSiNoNulo(fechaHardcodeadaDesde, fechaReporteHasta, PorFechaCarga::new, criterios);
        crearYAgregarSiNoNulo(fechaAcontecimientoDesde, fechaHardcodeadaHasta, PorFechaCarga::new, criterios);
        crearYAgregarSiNoNulo(fechaHardcodeadaDesde, fechaAcontecimientoHasta, PorFechaCarga::new, criterios);

        // Obtener y filtrar coleccion
        Coleccion unaColeccion = ColeccionRepositoryEnMemoria.getInstancia().buscarPorHandle(id);
        unaColeccion.filtrarHechos(criterios);

        ctx.json(unaColeccion);
    }

    public static <T, R> void crearYAgregarSiNoNulo(T valor, Function<T, R> constructor, Collection<R> destino) {
        if (valor != null) destino.add(constructor.apply(valor));
    }

    public static <T1, T2, R> void crearYAgregarSiNoNulo(T1 valor, T2 fijo, BiFunction<T1, T2, R> constructor, Collection<R> destino) {
        if (valor != null) destino.add(constructor.apply(valor, fijo));
    }

}
