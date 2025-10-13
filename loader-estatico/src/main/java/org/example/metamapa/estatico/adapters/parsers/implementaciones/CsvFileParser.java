package org.example.metamapa.estatico.adapters.parsers.implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.metamapa.estatico.adapters.parsers.IParserDeArchivo;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvFileParser implements IParserDeArchivo {

    @Override
    public List<HechoCrudo> parse(String nombreArchivo, byte[] contenido) throws IOException {
        List<HechoCrudo> hechos = new ArrayList<>();
        int filasValidas = 0;
        int filasInvalidas = 0;

        CSVFormat formato = detectarFormatoAdaptativo(contenido);

        try (Reader reader = new InputStreamReader(
                new ByteArrayInputStream(contenido), StandardCharsets.UTF_8)) {

            CSVParser parser = new CSVParser(reader, formato.withFirstRecordAsHeader());

            // Verificamos encabezados
            if (!tieneEncabezadosValidos(parser)) {
                log.warn("Encabezados inválidos. Reintentando lectura sin encabezados...");
                // Cerrar el parser y reabrir sin encabezado
                parser.close();

                Reader reader2 = new InputStreamReader(
                        new ByteArrayInputStream(contenido), StandardCharsets.UTF_8);

                // Reparseamos asumiendo que la primera línea es un registro normal
                parser = new CSVParser(reader2, formato.withHeader(
                        "Título", "Descripción", "Categoría", "Latitud", "Longitud", "Fecha del hecho"));
            }

            for (CSVRecord record : parser) {
                try {
                    HechoCrudo hecho = new HechoCrudo(
                            limpiarCampo(record.get("Título")),
                            limpiarCampo(record.get("Descripción")),
                            limpiarCampo(record.get("Categoría")),
                            limpiarCampo(record.get("Latitud")),
                            limpiarCampo(record.get("Longitud")),
                            limpiarCampo(record.get("Fecha del hecho"))
                    );
                    hecho.setFuenteOrigen(nombreArchivo);
                    hechos.add(hecho);
                    filasValidas++;

                } catch (Exception e) {
                    filasInvalidas++;
                    log.debug("Fila inválida: {}", e.getMessage());
                }
            }

            parser.close();

            log.info("Archivo CSV procesado: {} filas válidas, {} descartadas.", filasValidas, filasInvalidas);

        } catch (Exception e) {
            log.warn("Error procesando CSV: {}", e.getMessage());
            return List.of();
        }

        return hechos;
    }


    /**
     * Verifica si los encabezados esperados existen en el archivo.
     */
    private boolean tieneEncabezadosValidos(CSVParser parser) {
        var headers = parser.getHeaderMap().keySet().stream()
                .map(String::toLowerCase)
                .toList();

        return headers.contains("título") && headers.contains("descripción")
                && headers.contains("categoría") && headers.contains("latitud")
                && headers.contains("longitud") && headers.contains("fecha del hecho");
    }

    private CSVFormat detectarFormatoAdaptativo(byte[] contenido) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(contenido), StandardCharsets.UTF_8))) {

            int limite = 10;
            int lineasLeidas = 0;
            int comas = 0, puntosYComas = 0, tabs = 0;
            int lineasConComillas = 0;

            String linea;
            while ((linea = reader.readLine()) != null && lineasLeidas < limite) {
                if (linea.contains(",")) comas++;
                if (linea.contains(";")) puntosYComas++;
                if (linea.contains("\t")) tabs++;
                if (linea.contains("\"")) lineasConComillas++;
                lineasLeidas++;
            }

            // 🔹 Determinar delimitador más probable
            char delimitador = ',';
            if (puntosYComas > comas && puntosYComas >= tabs) delimitador = ';';
            else if (tabs > comas && tabs > puntosYComas) delimitador = '\t';

            // 🔹 Si solo la primera línea tiene comillas, las ignoramos
            boolean usarComillas = lineasConComillas > 1;

            CSVFormat formato = CSVFormat.DEFAULT
                    .withDelimiter(delimitador)
                    .withFirstRecordAsHeader()
                    .withTrim();

            if (usarComillas) formato = formato.withQuote('"');

            log.debug("Formato detectado → delimitador='{}', usoComillas={}, líneasAnalizadas={}",
                    delimitador, usarComillas, lineasLeidas);

            return formato;
        }
    }


    private String limpiarCampo(String valor) {
        if (valor == null) return "";
        return valor.replaceAll("^\"|\"$", "").trim();
    }
}
