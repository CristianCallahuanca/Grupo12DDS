package Fuentes.FuenteEstatica;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Fuente;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FuenteEstatica extends Fuente {

    Dataset dataset;

    public FuenteEstatica(Dataset dataset){
        this.dataset = dataset;
    }

    public boolean tieneMasDeDiezMilFilas() throws IOException {

        FileReader file = new FileReader(dataset.getArchivoCSV());
        int contarLineas = 0;

        try (CSVReader csvReader = new CSVReader(file)) {
            String[] parts = null;
            while ((parts = csvReader.readNext()) != null) {
                contarLineas = contarLineas + 1;

            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return contarLineas > 10000;
    }

    public List<Hecho> procesarHechosDesdeCSV() throws IOException {

        List<Hecho> hechos = new ArrayList<Hecho>();
        FileReader file = new FileReader(dataset.getArchivoCSV());
        Boolean primeraLinea = true;

        if(tieneMasDeDiezMilFilas()){

            try (CSVReader csvReader = new CSVReader(file)) {
                String[] parts = null;
                while ((parts = csvReader.readNext()) != null) {
                /*
                    System.out.println("Título: " + parts[0]);
                    System.out.println("Descripción: " + parts[1]);
                    System.out.println("Categoría: " + parts[2]);
                    System.out.println("Latitud: " + parts[3]);
                    System.out.println("Longitud: " + parts[4]);
                    System.out.println("Fecha: " + parts[5]);
                    System.out.println("------------------------");*/

                    if(!primeraLinea){
                        Ubicacion ubi = new Ubicacion(Double.parseDouble(parts[3]),Double.parseDouble(parts[4]));

                        // Definimos el formato del string de fecha
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        // Primero convertimos a LocalDate
                        LocalDate fecha = LocalDate.parse(parts[5], formatter);

                        // Convertimos LocalDate a LocalDateTime agregando la hora (00:00)
                        LocalDateTime fechaHora = fecha.atStartOfDay();

                        Hecho hecho = new Hecho(parts[0],parts[1],parts[2],ubi,fechaHora,LocalDateTime.now(),"hola");

                        hechos.add(hecho);
                    }

                    primeraLinea = false;

                }
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("El archivo tiene que tener mas de 10000 filas");
        }

        return hechos;
    }

    /*public void importarDesdeCSV() throws IOException {
        List<Hecho> nuevosHechos = dataset.procesarHechosDesdeCSV();

        for (Hecho nuevo : nuevosHechos) {

            hechos.removeIf(h -> h.getTitulo().equals(nuevo.getTitulo()));
            hechos.add(nuevo);
        }
    }*/
}

