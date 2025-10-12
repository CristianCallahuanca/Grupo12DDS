package org.example.metamapa.estatico.adapters.parsers;

import org.example.metamapa.estatico.adapters.parsers.implementaciones.CsvFileParser;
import org.example.metamapa.estatico.adapters.parsers.implementaciones.ExcelFileParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ParserFactory {

    private final Map<String, IParserDeArchivo> parsers = new HashMap<>();

    public ParserFactory() {
        parsers.put("csv", new CsvFileParser());
        parsers.put("xls", new ExcelFileParser());
        parsers.put("xlsx", new ExcelFileParser());
    }

    public IParserDeArchivo obtenerParser(String nombreArchivo) {
        String extension = obtenerExtension(nombreArchivo);
        return parsers.getOrDefault(extension, new CsvFileParser());
    }

    private String obtenerExtension(String nombreArchivo) {
        int idx = nombreArchivo.lastIndexOf('.');
        return (idx > 0) ? nombreArchivo.substring(idx + 1).toLowerCase() : "";
    }
}
