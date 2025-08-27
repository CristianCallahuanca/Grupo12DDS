package loader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileServerMemory implements AdapterFS{

    private final List<String> csvRepository;

    public FileServerMemory(List<String> csvRepository) {
        this.csvRepository = csvRepository;
    }

    @Override
    public String obtenerNuevoCSV(List<String> csvProcesados){
        if(csvProcesados == null && !csvRepository.isEmpty()){
            return csvRepository.get(0);
        }
        return csvRepository.stream().filter(c-> !csvProcesados.contains(c)).findFirst().orElse(null);
    }
}
