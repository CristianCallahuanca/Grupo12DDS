package loader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import org.example.metamapa.estatico.models.entidades.ElementoCSV;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.example.metamapa.estatico.models.repositorios.AdapterFS;
import org.example.metamapa.estatico.models.repositorios.implementaciones.RepositoryCSVProcesado;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Getter
public class FuenteEstatica {
    private static final FuenteEstatica instance = new FuenteEstatica();
    private AdapterFS adaptadorFileServer;

    //Singleton
    public static FuenteEstatica getInstancia() {
        return instance;
    }

    public void procesarCSV(int filasAProcesar) throws IOException {

        ElementoCSV archivoCSV = RepositoryCSVProcesado.getInstancia().csvALeer(this.adaptadorFileServer);
        Lectura_CSV_DTO var = this.leerCSVPorArchivo(archivoCSV, filasAProcesar);

        this.persistirCSVLeido(var.getElementoCSV());
        if(var.getHechoCrudos() != null) {
            this.persistirHechoCrudo(var.getHechoCrudos());
        }
    }

    private void persistirHechoCrudo(List<HechoCrudo> hechoCrudo) {
        //agregarlos a la BD del agregador
    }

    private void persistirCSVLeido(ElementoCSV elementoCSV){
        RepositoryCSVProcesado.getInstancia().actualizarArchivoCSV(elementoCSV);
    }

    public Lectura_CSV_DTO leerCSVPorArchivo(ElementoCSV csvAProcesar, int filasAProcesar) throws IOException {

        //Si el csv es "chico" lo marco cómo leido para no tenerlo en cuenta
        if(!tieneMasDeNFilas(10000, csvAProcesar.getArchivoCSV())){
            csvAProcesar.setProcesado(true);
            return new Lectura_CSV_DTO(csvAProcesar, null);
        }
        List<HechoCrudo> hechosCrudos = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(csvAProcesar.getArchivoCSV()))) {
            String[] parts;

            int filaActual = 0;
            int ultimaFilaLeida = csvAProcesar.getUltimaFilaLeida();
            boolean primeraLinea = true;

            // saltar hasta la ultimaFilaLeida
            while (filaActual < ultimaFilaLeida && (parts = csvReader.readNext()) != null) {
                filaActual++;
            }

            // ahora empezar a procesar solo las filas necesarias
            int filasProcesadas = 0;
            while (filasProcesadas < filasAProcesar && (parts = csvReader.readNext()) != null) {
                if (!primeraLinea) {
                    HechoCrudo hecho = new HechoCrudo(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    hechosCrudos.add(hecho);
                }
                primeraLinea = false;

                csvAProcesar.actualizarUltimaFilaLeida(); // vas actualizando la posición
                filasProcesadas++;
            }

            if((parts = csvReader.readNext()) == null){
                csvAProcesar.setProcesado(true);
            }
            csvAProcesar.setUltimaFilaLeida(ultimaFilaLeida + filasAProcesar);

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return new Lectura_CSV_DTO(csvAProcesar, hechosCrudos);
    }

    private boolean tieneMasDeNFilas(int minimoDeArchivos, String archivo) throws IOException {

        FileReader file = new FileReader(archivo);
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
}