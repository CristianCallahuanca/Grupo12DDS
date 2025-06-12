package Fuentes.FuenteEstatica;


import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Ubicacion;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Dataset {

    private String archivoCSV;

    public Dataset(String archivoCSV) {
        this.archivoCSV = archivoCSV;
        FuenteEstatica.getInstancia().guardar(this);
    }

    public String getArchivoCSV() {
        return archivoCSV;
    }

}

