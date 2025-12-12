package org.example.metamapa.estadisticas.Controladores;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estadisticas.Models.dtos.*;
import org.example.metamapa.estadisticas.Servicios.IEstadisticasConsultaService;
import org.example.metamapa.estadisticas.Servicios.IGeneracionCsvService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/estadisticas")
@RequiredArgsConstructor
@Slf4j
public class EstadisticasController {

    private final IEstadisticasConsultaService consultaService;
    private final IGeneracionCsvService generacionCsvService;

    // ─────────────────────────────
    // JSON para dashboards / graficos
    // ─────────────────────────────

    @GetMapping("/mayor-hechos-provincia-coleccion")
    public ResponseEntity<List<EstadMayorHechosPorProvinciaColeccionDTO>> getMayorHechosProvinciaColeccion() {

        List<EstadMayorHechosPorProvinciaColeccionDTO> datos =
                consultaService.obtenerMayorHechosProvinciaColeccion();
        if (datos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/categoria-mas-reportada")
    public ResponseEntity<List<EstadCategoriaMasReportadaDTO>> getCategoriaMasReportada() {

        List<EstadCategoriaMasReportadaDTO> datos =
                consultaService.obtenerCategoriaMasReportada();
        if (datos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/provincia-por-categoria")
    public ResponseEntity<List<EstadProvinciaPorCategoriaDTO>> getProvinciaPorCategoria() {

        List<EstadProvinciaPorCategoriaDTO> datos =
                consultaService.obtenerProvinciaPorCategoria();
        if (datos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/hora-por-categoria")
    public ResponseEntity<List<EstadHoraPorCategoriaDTO>> getHoraPorCategoria() {

        List<EstadHoraPorCategoriaDTO> datos =
                consultaService.obtenerHoraPorCategoria();
        if (datos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/cantidad-solicitudes-spam")
    public ResponseEntity<List<EstadCantidadSolicitudesSpamDTO>> getCantidadSolicitudesSpam() {

        List<EstadCantidadSolicitudesSpamDTO> datos =
                consultaService.obtenerCantidadSolicitudesSpam();
        if (datos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(datos);
    }

    // ─────────────────────────────
    // CSV POR TIPO DE ESTADISTICA
    // ─────────────────────────────

    @GetMapping("/mayor-hechos-provincia-coleccion/csv")
    public void exportMayorHechosProvinciaColeccionCsv(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response) throws IOException {


        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"mayor_hechos_provincia_coleccion.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            generacionCsvService.escribirMayorHechosProvinciaColeccionCsv(writer, desde, hasta);
        }

        log.info("CSV de mayor_hechos_provincia_coleccion enviado correctamente.");
    }

    @GetMapping("/categoria-mas-reportada/csv")
    public void exportCategoriaMasReportadaCsv(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"categoria_mas_reportada.csv\"");

        generacionCsvService.escribirCategoriaMasReportadaCsv(response.getOutputStream(), desde, hasta);

        log.info("CSV de categoria_mas_reportada enviado correctamente.");
    }

    @GetMapping("/provincia-por-categoria/csv")
    public void exportProvinciaPorCategoriaCsv(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"provincia_por_categoria.csv\"");

        generacionCsvService.escribirProvinciaPorCategoriaCsv(response.getOutputStream(), desde, hasta);

        log.info("CSV de provincia_por_categoria enviado correctamente.");
    }

    @GetMapping("/hora-por-categoria/csv")
    public void exportHoraPorCategoriaCsv(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"hora_por_categoria.csv\"");

        generacionCsvService.escribirHoraPorCategoriaCsv(response.getOutputStream(), desde, hasta);

        log.info("CSV de hora_por_categoria enviado correctamente.");
    }

    @GetMapping("/cantidad-solicitudes-spam/csv")
    public void exportCantidadSolicitudesSpamCsv(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"cantidad_solicitudes_spam.csv\"");

        generacionCsvService.escribirCantidadSolicitudesSpamCsv(response.getOutputStream(), desde, hasta);

        log.info("CSV de cantidad_solicitudes_spam enviado correctamente.");
    }
}
