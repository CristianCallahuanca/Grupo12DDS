package Persona;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import Fuentes.Fuente;
import AdministracionDeHechos.Coleccion;
import Fuentes.FuenteEstatica.FuenteEstatica;
import SolicitudEliminar.SolicitudEliminar;
import SolicitudEliminar.EstadoEliminar;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class Administrador {
    private String nombre;
    private String apellido;
    private int edad;

    public Administrador(String nombre, String apellido, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    public void crearColeccion(Fuente fuente, String titulo, String descripcion, List<CriterioDePertenencia> criterios, String handle) {
        Coleccion unaColeccion = new Coleccion( fuente,  titulo,  descripcion,  criterios, handle);

    }




    public void revisarSolicitud(SolicitudEliminar solicitud, boolean aprobar) {
        if (aprobar) {
            solicitud.aceptar();
        } else {
            solicitud.rechazar();
        }
    }

    /*public void revisarSolicitudesPendientes(List<SolicitudEliminar> solicitudes) {
        for (SolicitudEliminar solicitud : solicitudes) {
            if (solicitud.getEstadoEliminar() == EstadoEliminar.PENDIENTE) {

            }
        }
    }*/

    public void importarHechos(FuenteEstatica fuente, String rutaArchivo) {
        fuente.importarDesdeCSV(rutaArchivo);
    }


}


// No tiene sentido que administrador haga esto, en realidad lo deberia hacer el dataset, aca
//nosotros hacemos que el administrador inicie la accion del mismo no que haga todo
    /*public void leerHechos(String rutaArchivo){

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Leer la cabecera y descartarla
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",", -1); // -1 para que no ignore columnas vacías

                if (datos.length < 6) {
                    System.out.println("Línea con formato incorrecto: " + linea);
                    continue;
                }

                String titulo = datos[0];
                String descripcion = datos[1];
                String categoria = datos[2];
                String latitud = datos[3];
                String longitud = datos[4];
                String fecha = datos[5];

                System.out.println("----- Evento -----");
                System.out.println("Título: " + titulo);
                System.out.println("Descripción: " + descripcion);
                System.out.println("Categoría: " + categoria);
                System.out.println("Latitud: " + latitud);
                System.out.println("Longitud: " + longitud);
                System.out.println("Fecha: " + fecha);
                System.out.println();
            }
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }*/