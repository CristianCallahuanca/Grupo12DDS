package org.example.metamapa.estadisticas.Servicios.implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.metamapa.estadisticas.Models.entidades.*;
import org.example.metamapa.estadisticas.Models.repositorios.*;
import org.example.metamapa.estadisticas.Servicios.IGeneracionCsvService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
    public void escribirCategoriaMasReportadaCsv(OutputStream outputStream,
                                                 LocalDate desde,
                                                 LocalDate hasta) throws IOException {

        // Escribir BOM para UTF-8 (esto soluciona los acentos en Excel)
        outputStream.write(0xEF);
        outputStream.write(0xBB);
        outputStream.write(0xBF);

        CSVFormat format = CSVFormat.EXCEL
                .withDelimiter(';')
                .withHeader(
                        "fecha",
                        "categoria_id",
                        "categoria_nombre",
                        "cantidad_hechos"
                )
                .withSkipHeaderRecord(false);

        List<EstCategoriaMasReportada> lista =
                repoCatMasRep.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        try (CSVPrinter csvPrinter = new CSVPrinter(
                new OutputStreamWriter(outputStream, StandardCharsets.UTF_8),
                format)) {

            for (EstCategoriaMasReportada e : lista) {
                csvPrinter.printRecord(
                        e.getFechaCalculo(),
                        e.getCategoriaId(),
                        e.getCategoriaNombre(),
                        e.getCantidadHechos()
                );
            }

            csvPrinter.flush();
        }

        log.info("CSV generado por rango para categoria_mas_reportada. Registros: {}", lista.size());
    }

    // ─────────────────────────────
    // 3) Provincia por categoría
    // ─────────────────────────────

    @Override
    public void escribirProvinciaPorCategoriaCsv(OutputStream outputStream,
                                                 LocalDate desde,
                                                 LocalDate hasta) throws IOException {

        // Primero escribir el BOM (Byte Order Mark) para UTF-8
        outputStream.write(0xEF);
        outputStream.write(0xBB);
        outputStream.write(0xBF);

        // Usar UTF-8 explícitamente en todos los lugares
        CSVFormat format = CSVFormat.EXCEL
                .withDelimiter(';')
                .withHeader(
                        "fecha",
                        "categoria_id",
                        "categoria_nombre",
                        "provincia",
                        "cantidad_hechos"
                )
                .withSkipHeaderRecord(false);

        List<EstProvinciaPorCategoria> lista =
                repoProvPorCat.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        // Asegurar que el Writer use UTF-8 con BOM
        try (OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(osw, format)) {

            for (EstProvinciaPorCategoria e : lista) {
                csvPrinter.printRecord(
                        e.getFechaCalculo(),
                        e.getCategoriaId(),
                        e.getCategoriaNombre(),
                        e.getProvincia(),
                        e.getCantidadHechos()
                );
            }

            csvPrinter.flush();
        }

        log.info("CSV generado por rango para provincia_por_categoria. Registros: {}", lista.size());
    }

    // ─────────────────────────────
    // 4) Hora por categoría
    // ─────────────────────────────
    @Override
    public void escribirHoraPorCategoriaCsv(OutputStream outputStream,
                                            LocalDate desde,
                                            LocalDate hasta) throws IOException {

        // Escribir BOM para UTF-8
        outputStream.write(0xEF);
        outputStream.write(0xBB);
        outputStream.write(0xBF);

        CSVFormat format = CSVFormat.EXCEL
                .withDelimiter(';')
                .withHeader(
                        "fecha",
                        "categoria_id",
                        "categoria_nombre",
                        "hora",
                        "cantidad_hechos"
                )
                .withSkipHeaderRecord(false);

        List<EstHoraPorCategoria> lista =
                repoHoraPorCat.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        try (CSVPrinter csvPrinter = new CSVPrinter(
                new OutputStreamWriter(outputStream, StandardCharsets.UTF_8),
                format)) {

            for (EstHoraPorCategoria e : lista) {
                csvPrinter.printRecord(
                        e.getFechaCalculo(),
                        e.getCategoriaId(),
                        e.getCategoriaNombre(),
                        e.getHora(),
                        e.getCantidadHechos()
                );
            }

            csvPrinter.flush();
        }

        log.info("CSV generado por rango para hora_por_categoria. Registros: {}", lista.size());
    }

    // ─────────────────────────────
    // 5) Cantidad de solicitudes spam
    // ─────────────────────────────
    @Override
    public void escribirCantidadSolicitudesSpamCsv(OutputStream outputStream,
                                                   LocalDate desde,
                                                   LocalDate hasta) throws IOException {

        // Escribir BOM para UTF-8
        outputStream.write(0xEF);
        outputStream.write(0xBB);
        outputStream.write(0xBF);

        CSVFormat format = CSVFormat.EXCEL
                .withDelimiter(';')
                .withHeader(
                        "fecha",
                        "cantidad_spam"
                )
                .withSkipHeaderRecord(false);

        List<EstCantidadSolicitudesSpam> lista =
                repoSpam.findByFechaCalculoBetween(inicio(desde), fin(hasta));

        try (CSVPrinter csvPrinter = new CSVPrinter(
                new OutputStreamWriter(outputStream, StandardCharsets.UTF_8),
                format)) {

            for (EstCantidadSolicitudesSpam e : lista) {
                csvPrinter.printRecord(
                        e.getFechaCalculo(),
                        e.getCantidadSpam()
                );
            }

            csvPrinter.flush();
        }

        log.info("CSV generado por rango para cantidad_solicitudes_spam. Registros: {}", lista.size());
    }
}
