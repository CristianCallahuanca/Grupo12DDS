package org.example.metamapa.estadisticas.Servicios.implementaciones;

import org.example.metamapa.estadisticas.Models.entidades.EstadisticaGeneral;
import org.example.metamapa.estadisticas.Models.repositorios.IEstadisticasGenerales;
import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class EstadisticaService implements IEstadisticaService {


    private final IEstadisticasGenerales repoEstadisticas;

    EstadisticaService(IEstadisticasGenerales repoEstadisticas){
        this.repoEstadisticas = repoEstadisticas;
    }

    public List<EstadisticaGeneral> obtenerEstadisticas(){

        String url = "jdbc:mysql://localhost:3306/central";
        String user = "root";
        String password = "12345678";
        List<EstadisticaGeneral> listaEstadisticas = new ArrayList<EstadisticaGeneral>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = """
                SELECT * from estadistica_general
                """;

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                EstadisticaGeneral estadistica = new EstadisticaGeneral(rs.getString("tipo_estadistica"), String.valueOf(rs.getInt("cantidad_solicitudes_spam")),
                        rs.getString("categoria"), rs.getString("provincia"),  String.valueOf(rs.getInt("cantidad")), rs.getString("titulo"),
                        String.valueOf(rs.getInt("cantidad_hechos")), rs.getString("hora"));

                listaEstadisticas.add(estadistica);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEstadisticas;

    }


    public void generarEstadisticas(){

        String url = "jdbc:mysql://localhost:3306/central";
        String user = "root";
        String password = "12345678";

        repoEstadisticas.deleteAll();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // 🔹 PRIMERA CONSULTA
            String sql1 = """
                SELECT c.titulo, u.provincia, COUNT(DISTINCT h.hecho_id) AS cantidad_hechos
                FROM hecho AS h
                JOIN ubicacion AS u ON h.hecho_id = u.id
                JOIN hecho_de_coleccion AS hc ON hc.hecho_id = h.hecho_id
                JOIN coleccion AS c ON hc.coleccion_id = c.handle
                GROUP BY c.titulo, u.provincia
                ORDER BY cantidad_hechos DESC
                LIMIT 1;
                """;

            ResultSet rs1 = stmt.executeQuery(sql1);

            System.out.println("▶ Resultados de la primera consulta:");
            while (rs1.next()) {
                String titulo = rs1.getString("titulo");
                String provincia = rs1.getString("provincia");
                int cantHechos = rs1.getInt("cantidad_hechos");
                System.out.println(titulo + " - " + provincia + " - " + cantHechos);

                EstadisticaGeneral estadistica = new EstadisticaGeneral("mayor_cant_hechos_provincia", null, null, provincia, null, titulo, Integer.toString(cantHechos), null);
                repoEstadisticas.save(estadistica);
            }

            System.out.println("\n▶ Resultados de la segunda consulta:");

            String sql2 = """
                    SELECT
                                categoria,
                                COUNT(*) as cantidad
                            FROM hecho
                            WHERE categoria != 'Sin categoria'
                            GROUP BY categoria
                            ORDER BY cantidad DESC
                            LIMIT 1;
                """;

            ResultSet rs2 = stmt.executeQuery(sql2);
            int cont = 0;
            while (rs2.next()) {
                cont = cont + 1;
                String categoria = rs2.getString("categoria");
                int cantidad = rs2.getInt("cantidad");

                System.out.println("------------------------------");
                System.out.println("categoria: " + categoria);
                System.out.println("cantidad" + cantidad);

                EstadisticaGeneral estadistica2 = new EstadisticaGeneral("categoria_mas_reportada", null, categoria,null, Integer.toString(cantidad), null, null, null);
                repoEstadisticas.save(estadistica2);
            }

            if(cont == 0){
                EstadisticaGeneral estadistica2 = new EstadisticaGeneral("categoria_mas_reportada", null, "no_existe_categoria",null, null, null, null, null);
                repoEstadisticas.save(estadistica2);
            }

            // 🔹 SEGUNDA CONSULTA (nuevo query con el mismo Statement)
            String sql3 = """
                    WITH categorias AS (
                            SELECT 'vientos fuertes' as categoria
                            UNION SELECT 'inundaciones'
                            UNION SELECT 'granizo'
                            UNION SELECT 'nevadas'
                            UNION SELECT 'calor extremo'
                            UNION SELECT 'sequía'
                            UNION SELECT 'derrumbes'
                            UNION SELECT 'actividad volcánica'
                            UNION SELECT 'incendios'
                            UNION SELECT 'contaminación'
                            UNION SELECT 'evento sanitario'
                            UNION SELECT 'derrame'
                            UNION SELECT 'intoxicación masiva'
                        ),
                        maximos_por_categoria AS (
                            SELECT
                                h.categoria,
                                u.provincia,
                                COUNT(*) as cantidad
                            FROM hecho h
                            JOIN ubicacion u ON h.ubicacion_id = u.id
                            WHERE h.categoria IN (SELECT categoria FROM categorias)
                            GROUP BY h.categoria, u.provincia
                        ),
                        provincia_maxima AS (
                            SELECT
                                m1.categoria,
                                m1.provincia,
                                m1.cantidad
                            FROM maximos_por_categoria m1
                            WHERE m1.cantidad = (
                                SELECT MAX(m2.cantidad)
                                FROM maximos_por_categoria m2
                                WHERE m2.categoria = m1.categoria
                            )
                        )
                        SELECT
                            c.categoria,
                            COALESCE(pm.provincia, 'Sin datos') as provincia,
                            COALESCE(pm.cantidad, 0) as cantidad
                        FROM categorias c
                        LEFT JOIN provincia_maxima pm ON c.categoria = pm.categoria
                        ORDER BY c.categoria;
                """;

            ResultSet rs3 = stmt.executeQuery(sql3);

            System.out.println("\n▶ Resultados de la segunda consulta:");

            while (rs3.next()) {
                String categoria = rs3.getString("categoria");
                String provincia = rs3.getString("provincia");
                int cantidad = rs3.getInt("cantidad");

                System.out.println("------------------------------");
                System.out.println("categoria: " + categoria);
                System.out.println("provincia" + provincia);
                System.out.println("cantidad" + cantidad);

                EstadisticaGeneral estadistica3 = new EstadisticaGeneral("categoria_mas_reportada_por_provincia", null, categoria, provincia, Integer.toString(cantidad), null, null, null);
                repoEstadisticas.save(estadistica3);
            }

            System.out.println("\n▶ Resultados de la cuarta consulta:");

            String sql4 = """
                    WITH categorias AS (
                        SELECT 'vientos fuertes' AS categoria
                        UNION SELECT 'inundaciones'
                        UNION SELECT 'granizo'
                        UNION SELECT 'nevadas'
                        UNION SELECT 'calor extremo'
                        UNION SELECT 'sequía'
                        UNION SELECT 'derrumbes'
                        UNION SELECT 'actividad volcánica'
                        UNION SELECT 'incendios'
                        UNION SELECT 'contaminación'
                        UNION SELECT 'evento sanitario'
                        UNION SELECT 'derrame'
                        UNION SELECT 'intoxicación masiva'
                    ),
                    maximos_por_categoria AS (
                        SELECT
                            h.categoria,
                            HOUR(h.facha_acontecimiento) AS hora,
                            COUNT(*) AS cantidad
                        FROM hecho h
                        WHERE h.categoria IN (SELECT categoria FROM categorias)
                        GROUP BY h.categoria, HOUR(h.facha_acontecimiento)
                    ),
                    hora_maxima AS (
                        SELECT
                            m1.categoria,
                            m1.hora,
                            m1.cantidad
                        FROM maximos_por_categoria m1
                        WHERE m1.cantidad = (
                            SELECT MAX(m2.cantidad)
                            FROM maximos_por_categoria m2
                            WHERE m2.categoria = m1.categoria
                        )
                    )
                    SELECT
                        c.categoria,
                        COALESCE(hm.hora, NULL) AS hora,
                        COALESCE(hm.cantidad, 0) AS cantidad
                    FROM categorias c
                    LEFT JOIN hora_maxima hm ON c.categoria = hm.categoria
                    ORDER BY c.categoria;
                    """;

            ResultSet rs4 = stmt.executeQuery(sql4);

            while (rs4.next()) {
                String categoria = rs4.getString("categoria");
                int hora = rs4.getInt("hora");
                int cantidad = rs4.getInt("cantidad");

                System.out.println("------------------------------");
                System.out.println("categoria: " + categoria);
                System.out.println("hora: " + hora);
                System.out.println("cantidad: " + cantidad);
                EstadisticaGeneral estadistica4 = new EstadisticaGeneral("categoria_mas_reportada_por_hora", null, categoria, null, Integer.toString(cantidad), null, null, Integer.toString(hora));
                repoEstadisticas.save(estadistica4);
            }

            System.out.println("\n▶ Resultados de la quinta consulta:");

            String sql5 = """
                    select count(*) as cant_solicitudes_spam from solicitud_eliminacion
                    where verifico_si_es_spam = TRUE
                    """;

            ResultSet rs5 = stmt.executeQuery(sql5);

            while(rs5.next()){
                int cantidad_spam = rs5.getInt("cant_solicitudes_spam");
                System.out.println("cantidad de spam: " + cantidad_spam);
                EstadisticaGeneral estadistica5 = new EstadisticaGeneral("cantidad_solicitudes_spam", Integer.toString(cantidad_spam), null,null, null, null, null, null);
                repoEstadisticas.save(estadistica5);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
