package org.example.metamapa.estatico.models.repositorios.implementaciones;

import org.example.metamapa.estatico.models.repositorios.AdapterFS;
import java.util.List;

public class FileServerMemory implements AdapterFS {

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
