package Fuentes.FuenteEstatica;


import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Dataset {
    private List<String> archivosCSV;

    public Dataset(List<String> archivosCSV) {
        this.archivosCSV = archivosCSV;
    }

    public List<Hecho> procesarHechosDesdeCSV(String rutaArchivo) {
        List<Hecho> hechos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine(); // Saltar encabezado

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                if (datos.length < 6) continue;

                Hecho hecho = new Hecho(
                        datos[0], // título
                        datos[1], // descripcion
                        datos[2], // categoría
                        new Ubicacion(Double.parseDouble(datos[3]), Double.parseDouble(datos[4])),
                        LocalDateTime.parse(datos[5]), // fecha acontecimiento
                        LocalDateTime.now(),           // fecha carga
                        ""                             // etiqueta
                );


                hechos.add(hecho);
            }
        } catch (IOException e) {
            System.err.println("Error al procesar CSV: " + e.getMessage());
        }

        return hechos;
    }
}

