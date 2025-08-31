package org.example.metamapa.estatico.models.repositorios.implementaciones;

import org.example.metamapa.estatico.models.repositorios.AdapterFS;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileServerMemory implements AdapterFS {

    private final List<String> csvRepository = new ArrayList<>();

    public FileServerMemory() {
        // Simulación de archivos disponibles (rutas relativas al proyecto)
        csvRepository.add("datos/desastres_naturales_argentina.csv");
        csvRepository.add("datos/desastres_sanitarios_contaminacion_argentina.csv");
        csvRepository.add("datos/desastres_tecnologicos_argentina.csv");
    }

    @Override
    public String obtenerNuevoCSV(List<String> csvProcesados) {
        if (csvProcesados == null && !csvRepository.isEmpty()) {
            return csvRepository.get(0);
        }
        return csvRepository.stream()
                .filter(csv -> !csvProcesados.contains(csv))
                .findFirst()
                .orElse(null);
    }
}
