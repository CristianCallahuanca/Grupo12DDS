package org.example.metamapa.estadisticas.Servicios.implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estadisticas.Models.entidades.EstadisticaGeneral;
import org.example.metamapa.estadisticas.Models.repositorios.IEstadisticasGenerales;
import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EstadisticaService implements IEstadisticaService {

    private final IEstadisticasGenerales repoEstadisticas;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    public EstadisticaService(IEstadisticasGenerales repoEstadisticas) {
        this.repoEstadisticas = repoEstadisticas;
    }

    @Override
    public List<EstadisticaGeneral> obtenerEstadisticas() {
        List<EstadisticaGeneral> lista = new ArrayList<>();

        String sql = "SELECT * FROM estadistica_general ORDER BY fecha DESC";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new EstadisticaGeneral(
                        rs.getString("tipo_estadistica"),
                        rs.getString("categoria"),
                        rs.getString("provincia"),
                        rs.getString("cantidad"),
                        rs.getString("titulo"),
                        rs.getString("hora")
                ));

            }

            log.info("Se recuperaron {} estadísticas almacenadas.", lista.size());

        } catch (SQLException e) {
            log.error("Error al obtener estadísticas desde la base de datos", e);
        }

        return lista;
    }


    @Override
    public void generarEstadisticas() {
        repoEstadisticas.deleteAll();
        log.info("Estadísticas anteriores eliminadas. Generando nuevas...");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             Statement stmt = conn.createStatement()) {

            generarMayorCantidadHechosPorProvincia(stmt);
            generarCategoriaMasReportada(stmt);
            generarProvinciaPorCategoria(stmt);
            generarHoraPorCategoria(stmt);
            generarCantidadSolicitudesSpam(stmt);

            log.info("Generación de estadísticas completada correctamente.");

        } catch (SQLException e) {
            log.error("Error al generar estadísticas", e);
        }
    }


    private void generarMayorCantidadHechosPorProvincia(Statement stmt) throws SQLException {
        String sql = """
                SELECT c.titulo AS titulo_coleccion, u.provincia, COUNT(h.hecho_id) AS cantidad_hechos
                FROM hecho h
                JOIN ubicacion u ON h.ubicacion_id = u.id
                JOIN hecho_de_coleccion hc ON hc.hecho_id = h.hecho_id
                JOIN coleccion c ON hc.coleccion_id = c.handle
                GROUP BY c.titulo, u.provincia
                ORDER BY cantidad_hechos DESC
                LIMIT 1;
                """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                EstadisticaGeneral e = new EstadisticaGeneral(
                        "mayor_cant_hechos_provincia",
                        null,
                        rs.getString("provincia"),
                        String.valueOf(rs.getInt("cantidad_hechos")),
                        null,
                        null
                );
                repoEstadisticas.save(e);
            }
        }
        log.info("Consulta 1 completada: Mayor cantidad de hechos por provincia.");
    }

    private void generarCategoriaMasReportada(Statement stmt) throws SQLException {
        String sql = """
                SELECT cat.nombre AS categoria, COUNT(h.hecho_id) AS cantidad
                FROM hecho h
                JOIN categorias cat ON h.categoria_id = cat.id
                WHERE h.categoria_id IS NOT NULL
                GROUP BY cat.nombre
                ORDER BY cantidad DESC
                LIMIT 1;
                """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                repoEstadisticas.save(new EstadisticaGeneral(
                        "categoria_mas_reportada",
                        rs.getString("categoria"),
                        null,
                        String.valueOf(rs.getInt("cantidad")),
                        null,
                        null
                ));
            } else {
                repoEstadisticas.save(new EstadisticaGeneral(
                        "categoria_mas_reportada",
                        "sin_categoria",
                        null,
                        "0",
                        null,
                        null
                ));
            }
        }
        log.info("Consulta 2 completada: Categoría más reportada.");
    }

    private void generarProvinciaPorCategoria(Statement stmt) throws SQLException {
        String sql = """
                WITH conteo AS (
                    SELECT\s
                        cat.nombre AS categoria,\s
                        u.provincia,\s
                        COUNT(h.hecho_id) AS cantidad
                    FROM hecho h
                    JOIN categorias cat ON h.categoria_id = cat.id
                    JOIN ubicacion u ON h.ubicacion_id = u.id
                    WHERE h.categoria_id IS NOT NULL
                    GROUP BY cat.nombre, u.provincia
                )
                SELECT c1.categoria, c1.provincia, c1.cantidad
                FROM conteo c1
                WHERE c1.cantidad = (
                    SELECT MAX(c2.cantidad) FROM conteo c2 WHERE c2.categoria = c1.categoria
                )
                ORDER BY c1.categoria
                LIMIT 1
                """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                repoEstadisticas.save(new EstadisticaGeneral(
                        "categoria_mas_reportada_por_provincia",
                        rs.getString("categoria"),
                        rs.getString("provincia"),
                        String.valueOf(rs.getInt("cantidad")),
                        null,
                        null
                ));
            }
        }
        log.info("Consulta 3 completada: Provincia con más hechos por categoría.");
    }

    private void generarHoraPorCategoria(Statement stmt) throws SQLException {
        String sql = """
                WITH conteo AS (
                    SELECT\s
                        cat.nombre AS categoria,\s
                        HOUR(h.fecha_acontecimiento) AS hora,\s
                        COUNT(h.hecho_id) AS cantidad
                    FROM hecho h
                    JOIN categorias cat ON h.categoria_id = cat.id
                    WHERE h.categoria_id IS NOT NULL
                    GROUP BY cat.nombre, HOUR(h.fecha_acontecimiento)
                )
                SELECT c1.categoria, c1.hora, c1.cantidad
                FROM conteo c1
                WHERE c1.cantidad = (
                    SELECT MAX(c2.cantidad) FROM conteo c2 WHERE c2.categoria = c1.categoria
                )
                ORDER BY c1.categoria
                LIMIT 1
                """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                repoEstadisticas.save(new EstadisticaGeneral(
                        "categoria_mas_reportada_por_hora",
                        rs.getString("categoria"),
                        null,
                        String.valueOf(rs.getInt("cantidad")),
                        null,
                        String.valueOf(rs.getInt("hora"))
                ));
            }
        }
        log.info("Consulta 4 completada: Hora con más hechos por categoría.");
    }

    private void generarCantidadSolicitudesSpam(Statement stmt) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS cantidad_spam
                FROM solicitud_eliminacion
                WHERE verifico_si_es_spam = TRUE;
                """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                repoEstadisticas.save(new EstadisticaGeneral(
                        "cantidad_solicitudes_spam",
                        null,
                        null,
                        String.valueOf(rs.getInt("cantidad_spam")),
                        null,
                        null
                ));
            }
        }
        log.info("Consulta 5 completada: Cantidad de solicitudes de spam.");
    }
}
