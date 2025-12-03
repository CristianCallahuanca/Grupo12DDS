package org.example.metamapa.estadisticas.Servicios.implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estadisticas.Models.entidades.*;
import org.example.metamapa.estadisticas.Models.repositorios.*;
import org.example.metamapa.estadisticas.Servicios.IGeneracionCsvService;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneracionCsvService implements IGeneracionCsvService {

    private final IEstHechosPorProvinciaColeccionRepository repoMayorHechos;
    private final IEstCategoriaMasReportadaRepository repoCatMasRep;
    private final IEstProvinciaPorCategoriaRepository repoProvPorCat;
    private final IEstHoraPorCategoriaRepository repoHoraPorCat;
    private final IEstCantidadSolicitudesSpamRepository repoSpam;

    private LocalDateTime inicio(LocalDate d) {
        return d.atStartOfDay();
    }

    private LocalDateTime fin(LocalDate d) {
        return d.atTime(LocalTime.MAX);
    }

    // ─────────────────────────────
    // 1) Mayor hechos por provincia y colección
    // ─────────────────────────────
    @Override
    public void escribirMayorHechosProvinciaColeccionCsv(PrintWriter writer,
                                                         LocalDate desde,
                                                         LocalDate hasta) {

        writer.println("fecha_calculo,coleccion_handle,coleccion_titulo,provincia,cantidad_hechos");


        List<EstHechosPorProvinciaColeccion> lista =
                repoMayorHechos.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        lista.forEach(e -> writer.printf("%s,%s,%s,%s,%d%n",
                e.getFechaCalculo(),
                e.getColeccionHandle(),
                e.getColeccionTitulo(),
                e.getProvincia(),
                e.getCantidadHechos()));

        log.info("CSV generado por rango para mayor_hechos_por_provincia_coleccion. Registros: {}", lista.size());
    }

    // ─────────────────────────────
    // 2) Categoría más reportada
    // ─────────────────────────────
    @Override
    public void escribirCategoriaMasReportadaCsv(PrintWriter writer,
                                                 LocalDate desde,
                                                 LocalDate hasta) {

        writer.println("fecha_calculo,categoria_id,categoria_nombre,cantidad_hechos");


        List<EstCategoriaMasReportada> lista =
                repoCatMasRep.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        lista.forEach(e -> writer.printf("%s,%d,%s,%d%n",
                e.getFechaCalculo(),
                e.getCategoriaId(),
                e.getCategoriaNombre(),
                e.getCantidadHechos()));

        log.info("CSV generado por rango para categoria_mas_reportada. Registros: {}", lista.size());
    }

    // ─────────────────────────────
    // 3) Provincia por categoría
    // ─────────────────────────────
    @Override
    public void escribirProvinciaPorCategoriaCsv(PrintWriter writer,
                                                 LocalDate desde,
                                                 LocalDate hasta) {

        writer.println("fecha_calculo,categoria_id,categoria_nombre,provincia,cantidad_hechos");

        List<EstProvinciaPorCategoria> lista =
                repoProvPorCat.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        lista.forEach(e -> writer.printf("%s,%d,%s,%s,%d%n",
                e.getFechaCalculo(),
                e.getCategoriaId(),
                e.getCategoriaNombre(),
                e.getProvincia(),
                e.getCantidadHechos()));

        log.info("CSV generado por rango para provincia_por_categoria. Registros: {}", lista.size());
    }

    // ─────────────────────────────
    // 4) Hora por categoría
    // ─────────────────────────────
    @Override
    public void escribirHoraPorCategoriaCsv(PrintWriter writer,
                                            LocalDate desde,
                                            LocalDate hasta) {

        writer.println("fecha_calculo,categoria_id,categoria_nombre,hora,cantidad_hechos");

        List<EstHoraPorCategoria> lista =
                repoHoraPorCat.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        lista.forEach(e -> writer.printf("%s,%d,%s,%d,%d%n",
                e.getFechaCalculo(),
                e.getCategoriaId(),
                e.getCategoriaNombre(),
                e.getHora(),
                e.getCantidadHechos()));

        log.info("CSV generado por rango para hora_por_categoria. Registros: {}", lista.size());
    }

    // ─────────────────────────────
    // 5) Cantidad de solicitudes spam
    // ─────────────────────────────
    @Override
    public void escribirCantidadSolicitudesSpamCsv(PrintWriter writer,
                                                   LocalDate desde,
                                                   LocalDate hasta) {

        writer.println("fecha_calculo,cantidad_spam");


        List<EstCantidadSolicitudesSpam> lista =
                repoSpam.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        lista.forEach(e -> writer.printf("%s,%d%n",
                e.getFechaCalculo(),
                e.getCantidadSpam()));

        log.info("CSV generado por rango para cantidad_solicitudes_spam. Registros: {}", lista.size());
    }
}
