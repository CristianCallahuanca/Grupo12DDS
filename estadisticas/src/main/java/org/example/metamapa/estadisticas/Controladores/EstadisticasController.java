package org.example.metamapa.estadisticas.Controladores;


import jakarta.servlet.http.HttpServletResponse;
import org.example.metamapa.estadisticas.Models.entidades.EstadisticaGeneral;
import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("estadisticas")
public class EstadisticasController {

    private final IEstadisticaService estadisticaService;

    public EstadisticasController(IEstadisticaService estadisticaService){
        this.estadisticaService = estadisticaService;
    }

    @GetMapping("/facts/csv")
    public void exportFactsToCsv(HttpServletResponse response) throws IOException {

        // Configurar respuesta para descarga
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"estadisticas_metamapa.csv\"");

        List<EstadisticaGeneral> estadisticas = estadisticaService.obtenerEstadisticas();


        try (PrintWriter writer = response.getWriter()) {
            // Escribir headers
            writer.println("ID,Fecha,Tipo_estadistica,Titulo,Cantidad,Cantidad_hechos,Cant_solicitudes_spam,Categoria,Hora,Provincia");

            int indice = 1;
            for(EstadisticaGeneral est : estadisticas){

                String sb = indice + "," +
                        est.getFecha() + "," +
                        est.getTipo_estadistica() + "," +
                        est.getTitulo() + "," +
                        est.getCantidad() + "," +
                        est.getCantidadHechos() + "," +
                        est.getCantidad_solicitudes_spam() + "," +
                        est.getCategoria() + "," +
                        est.getHora() + "," +
                        est.getProvincia();

                writer.println(sb);

                indice = indice + 1;
            }

        }

    }
}