package org.example.metamapa.estatico.adapters.implementaciones;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class CsvParserUtil {

    public static List<HechoCrudo> parsearDesdeBytes(byte[] contenido) throws IOException {
        List<HechoCrudo> hechos = new ArrayList<>();

        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(contenido), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> registros = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withTrim()
                    .withQuote('"')
                    .parse(reader);

            for (CSVRecord record : registros) {
                HechoCrudo hecho = new HechoCrudo(
                        record.get("Título"),
                        record.get("Descripción"),
                        record.get("Categoría"),
                        record.get("Latitud"),
                        record.get("Longitud"),
                        record.get("Fecha del hecho")
                );
                hechos.add(hecho);
            }
        }

        return hechos;
    }
}
