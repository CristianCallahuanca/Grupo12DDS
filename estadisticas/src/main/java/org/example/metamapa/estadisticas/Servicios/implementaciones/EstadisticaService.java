package org.example.metamapa.estadisticas.Servicios.implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estadisticas.Models.entidades.*;
import org.example.metamapa.estadisticas.Models.repositorios.*;
import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

@Service
@Slf4j
public class EstadisticaService implements IEstadisticaService {

    private final IEstHechosPorProvinciaColeccionRepository repoMayorHechos;
    private final IEstCategoriaMasReportadaRepository repoCatMasRep;
    private final IEstProvinciaPorCategoriaRepository repoProvPorCat;
    private final IEstHoraPorCategoriaRepository repoHoraPorCat;
    private final IEstCantidadSolicitudesSpamRepository repoSpam;
    private final DataSource dataSource;


    public EstadisticaService(IEstHechosPorProvinciaColeccionRepository repoMayorHechos,
                              IEstCategoriaMasReportadaRepository repoCatMasRep,
                              IEstProvinciaPorCategoriaRepository repoProvPorCat,
                              IEstHoraPorCategoriaRepository repoHoraPorCat,
                              IEstCantidadSolicitudesSpamRepository repoSpam,
                              DataSource dataSource) {

        this.repoMayorHechos = repoMayorHechos;
        this.repoCatMasRep = repoCatMasRep;
        this.repoProvPorCat = repoProvPorCat;
        this.repoHoraPorCat = repoHoraPorCat;
        this.repoSpam = repoSpam;
        this.dataSource = dataSource;
    }

    @Override
    public void generarEstadisticas() {
        log.info("Iniciando generación de estadísticas...");

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            LocalDateTime ahora = LocalDateTime.now();

            generarMayorCantidadHechosPorProvinciaColeccion(stmt, ahora);
            generarCategoriaMasReportada(stmt, ahora);
            generarProvinciaPorCategoria(stmt, ahora);
            generarHoraPorCategoria(stmt, ahora);
            generarCantidadSolicitudesSpam(stmt, ahora);

            log.info("Generación de estadísticas completada correctamente.");

        } catch (SQLException e) {
            log.error("Error al generar estadísticas", e);
        }
    }

    // -------------------------------------------------------------------------
    // 1) De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos?
    // -------------------------------------------------------------------------
    private void generarMayorCantidadHechosPorProvinciaColeccion(Statement stmt,
                                                                 LocalDateTime fechaCalculo) throws SQLException {

        String sql = """
                SELECT
                        c.handle AS coleccion_handle,
                        c.titulo AS coleccion_titulo,
                        (
                            SELECT u.provincia
                            FROM hecho h
                            JOIN ubicacion u ON h.ubicacion_id = u.id
                            JOIN hecho_de_coleccion hc ON hc.hecho_id = h.hecho_id
                            WHERE hc.coleccion_id = c.handle
                            GROUP BY u.provincia
                            ORDER BY COUNT() DESC, u.provincia ASC
                            LIMIT 1
                        ) AS provincia_top,
                        (
                            SELECT COUNT()
                            FROM hecho h2
                            JOIN ubicacion u2 ON h2.ubicacion_id = u2.id
                            JOIN hecho_de_coleccion hc2 ON hc2.hecho_id = h2.hecho_id
                            WHERE hc2.coleccion_id = c.handle
                            GROUP BY u2.provincia
                            ORDER BY COUNT(*) DESC, u2.provincia ASC
                            LIMIT 1
                        ) AS cantidad_hechos_top
                FROM coleccion c
                WHERE EXISTS (
                            SELECT 1
                            FROM hecho_de_coleccion hc
                            WHERE hc.coleccion_id = c.handle
                            )
                ORDER BY c.titulo;
                """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EstHechosPorProvinciaColeccion est = new EstHechosPorProvinciaColeccion();
                est.setFechaCalculo(fechaCalculo);
                est.setColeccionHandle(rs.getString("coleccion_handle"));
                est.setColeccionTitulo(rs.getString("coleccion_titulo"));
                est.setProvincia(rs.getString("provincia_top"));
                est.setCantidadHechos(rs.getInt("cantidad_hechos_top"));

                repoMayorHechos.save(est);
            }
        }

        log.info("Consulta 1 completada: Mayor cantidad de hechos por provincia y colección.");
    }

    // -------------------------------------------------------------------------
    // 2) ¿Cuál es la categoría con mayor cantidad de hechos reportados?
    // -------------------------------------------------------------------------
    private void generarCategoriaMasReportada(Statement stmt,
                                              LocalDateTime fechaCalculo) throws SQLException {

        String sql = """
                SELECT
                    cat.id     AS categoria_id,
                    cat.nombre AS categoria_nombre,
                    COUNT(h.hecho_id) AS cantidad_hechos
                FROM hecho h
                JOIN categorias cat ON h.categoria_id = cat.id
                WHERE h.categoria_id IS NOT NULL
                GROUP BY cat.id, cat.nombre
                ORDER BY cantidad_hechos DESC
                LIMIT 1
                """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                EstCategoriaMasReportada est = new EstCategoriaMasReportada();
                est.setFechaCalculo(fechaCalculo);
                est.setCategoriaId(rs.getLong("categoria_id"));
                est.setCategoriaNombre(rs.getString("categoria_nombre"));
                est.setCantidadHechos(rs.getInt("cantidad_hechos"));

                repoCatMasRep.save(est);
            } else {
                // snapshot “vacío”
                EstCategoriaMasReportada est = new EstCategoriaMasReportada();
                est.setFechaCalculo(fechaCalculo);
                est.setCategoriaNombre("sin_categoria");
                est.setCantidadHechos(0);
                repoCatMasRep.save(est);
            }
        }

        log.info("Consulta 2 completada: Categoría más reportada.");
    }

    // -------------------------------------------------------------------------
    // 3) ¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
    //    -> para cada categoría, la provincia top
    // -------------------------------------------------------------------------
    private void generarProvinciaPorCategoria(Statement stmt,
                                              LocalDateTime fechaCalculo) throws SQLException {

        String sql = """
            SELECT
                cat.id     AS categoria_id,
                cat.nombre AS categoria_nombre,
                (
                    SELECT u.provincia
                    FROM hecho h
                    JOIN ubicacion u ON h.ubicacion_id = u.id
                    WHERE h.categoria_id = cat.id
                    GROUP BY u.provincia
                    ORDER BY COUNT(h.hecho_id) DESC
                    LIMIT 1
                ) AS provincia_top,
                (
                    SELECT COUNT(h2.hecho_id)
                    FROM hecho h2
                    JOIN ubicacion u2 ON h2.ubicacion_id = u2.id
                    WHERE h2.categoria_id = cat.id
                    GROUP BY u2.provincia
                    ORDER BY COUNT(h2.hecho_id) DESC
                    LIMIT 1
                ) AS cantidad_hechos_top
            FROM categorias cat
            WHERE EXISTS (
                SELECT 1
                FROM hecho h
                WHERE h.categoria_id = cat.id
            )
            ORDER BY cat.nombre;
            """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EstProvinciaPorCategoria est = new EstProvinciaPorCategoria();
                est.setFechaCalculo(fechaCalculo);
                est.setCategoriaId(rs.getLong("categoria_id"));
                est.setCategoriaNombre(rs.getString("categoria_nombre"));
                est.setProvincia(rs.getString("provincia_top"));
                est.setCantidadHechos(rs.getInt("cantidad_hechos_top"));

                repoProvPorCat.save(est);
            }
        }

        log.info("Consulta 3 completada: Provincia con más hechos por categoría.");
    }


    // -------------------------------------------------------------------------
    // 4) ¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
    //    -> para cada categoría, la hora (0-23) con más hechos
    // -------------------------------------------------------------------------
    private void generarHoraPorCategoria(Statement stmt,
                                         LocalDateTime fechaCalculo) throws SQLException {

        String sql = """
            SELECT
                cat.id     AS categoria_id,
                cat.nombre AS categoria_nombre,
                (
                    SELECT HOUR(h.fecha_acontecimiento)
                    FROM hecho h
                    WHERE h.categoria_id = cat.id
                    GROUP BY HOUR(h.fecha_acontecimiento)
                    ORDER BY COUNT(h.hecho_id) DESC
                    LIMIT 1
                ) AS hora_top,
                (
                    SELECT COUNT(h2.hecho_id)
                    FROM hecho h2
                    WHERE h2.categoria_id = cat.id
                    GROUP BY HOUR(h2.fecha_acontecimiento)
                    ORDER BY COUNT(h2.hecho_id) DESC
                    LIMIT 1
                ) AS cantidad_hechos_top
            FROM categorias cat
            WHERE EXISTS (
                SELECT 1
                FROM hecho h
                WHERE h.categoria_id = cat.id
            )
            ORDER BY cat.nombre;
            """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EstHoraPorCategoria est = new EstHoraPorCategoria();
                est.setFechaCalculo(fechaCalculo);
                est.setCategoriaId(rs.getLong("categoria_id"));
                est.setCategoriaNombre(rs.getString("categoria_nombre"));
                est.setHora(rs.getInt("hora_top"));
                est.setCantidadHechos(rs.getInt("cantidad_hechos_top"));

                repoHoraPorCat.save(est);
            }
        }

        log.info("Consulta 4 completada: Hora con más hechos por categoría.");
    }


    // -------------------------------------------------------------------------
    // 5) ¿Cuántas solicitudes de eliminación son spam?
    // -------------------------------------------------------------------------
    private void generarCantidadSolicitudesSpam(Statement stmt,
                                                LocalDateTime fechaCalculo) throws SQLException {

        String sql = """
                SELECT COUNT(*) AS cantidad_spam
                FROM solicitud_eliminacion
                WHERE verifico_si_es_spam = TRUE;
                """;

        try (ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                EstCantidadSolicitudesSpam est = new EstCantidadSolicitudesSpam();
                est.setFechaCalculo(fechaCalculo);
                est.setCantidadSpam(rs.getInt("cantidad_spam"));

                repoSpam.save(est);
            }
        }

        log.info("Consulta 5 completada: Cantidad de solicitudes de spam.");
    }
}
