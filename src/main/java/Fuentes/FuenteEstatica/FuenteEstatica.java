package Fuentes.FuenteEstatica;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import Fuentes.Fuente;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FuenteEstatica extends Fuente {
    private static final FuenteEstatica instance = new FuenteEstatica();
    List<Dataset> ListaDeDatasets = new ArrayList<>();

    //Singleton
    public FuenteEstatica(){
    }

    public static FuenteEstatica getInstancia() {
        return instance;
    }

    //

    public boolean tieneMasDeDiezMilFilas(int minimoDeArchivos, Dataset unDataset) throws IOException {

        FileReader file = new FileReader(unDataset.getArchivoCSV());
        int contarLineas = 0;

        try (CSVReader csvReader = new CSVReader(file)) {
            String[] parts = null;
            while ((parts = csvReader.readNext()) != null) {
                contarLineas = contarLineas + 1;

            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return contarLineas > minimoDeArchivos;
    }

    public void guardar(Dataset unDataset) {
        ListaDeDatasets.add(unDataset);
    }

    public List<Hecho> procesarHechosDesdeCSV() throws IOException {
        int indice=0;
        List<Hecho> hechos = new ArrayList<Hecho>();
        while ( indice < ListaDeDatasets.size() ){

            FileReader file = new FileReader(ListaDeDatasets.get(indice).getArchivoCSV());

            //Esto es valido para el.CSV de prueba, pero dudo que sea valido para los demas
            Boolean primeraLinea = true;

            if (tieneMasDeDiezMilFilas(10000, ListaDeDatasets.get(indice))) {

                try (CSVReader csvReader = new CSVReader(file)) {
                    String[] parts = null;
                    while ((parts = csvReader.readNext()) != null) {

                        if (!primeraLinea) {
                            Ubicacion ubi = new Ubicacion(Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));

                            // Definimos el formato del string de fecha
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                            // Primero convertimos a LocalDate
                            LocalDate fecha = LocalDate.parse(parts[5], formatter);

                            // Convertimos LocalDate a LocalDateTime agregando la hora (00:00)
                            LocalDateTime fechaHora = fecha.atStartOfDay();

                            Origen origen = Origen.ESTATICA;

                            Hecho hecho = new Hecho(parts[0], parts[1], parts[2], ubi, fechaHora, LocalDateTime.now(), "", null, origen, null);

                            hechos.add(hecho);
                        }

                        primeraLinea = false;

                    }
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("El archivo tiene que tener mas de 10000 filas");
            }
        }
        return hechos;
    }

    @Override
    public List<Hecho> filtrarHechos(List<CriterioDePertenencia> criterios) throws IOException{
        List<Hecho> losHechos = this.procesarHechosDesdeCSV();
        return losHechos.stream().filter(unHecho -> unHecho.filtarHecho(criterios))
                    .toList();
    }

    public Hecho buscarPorTitulo(String titulo) throws IOException {

        List<Hecho> hechos = this.procesarHechosDesdeCSV();

        for (Hecho h : hechos) {
            if (h.getTitulo().equals(titulo)) {
                return h;
            }
        }
        return null;

    }
}







/*
                    System.out.println("Título: " + parts[0]);
                    System.out.println("Descripción: " + parts[1]);
                    System.out.println("Categoría: " + parts[2]);
                    System.out.println("Latitud: " + parts[3]);
                    System.out.println("Longitud: " + parts[4]);
                    System.out.println("Fecha: " + parts[5]);
                    System.out.println("------------------------");*/

