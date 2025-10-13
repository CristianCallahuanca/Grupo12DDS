package org.example.metamapa.agregador.service.implementacion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.Origen;
import org.example.metamapa.agregador.models.entidades.Ubicacion;
import org.example.metamapa.agregador.service.INormalizacionService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NormalizacionService implements INormalizacionService {

    //Atte GPT:
    public static String obtenerProvinciaAPI(double lat, double lon) {
        try {
            // Forzamos formato US: punto decimal, no coma
            String latStr = String.format(java.util.Locale.US, "%.6f", lat);
            String lonStr = String.format(java.util.Locale.US, "%.6f", lon);

            String urlString = String.format(
                    "https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json",
                    latStr, lonStr
            );

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "MetaMapa-Agregador/1.0 (UTN)");

            if (conn.getResponseCode() != 200) {
                System.err.println("Nominatim respondió HTTP " + conn.getResponseCode() + " para lat=" + latStr + ", lon=" + lonStr);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JsonNode root = new ObjectMapper().readTree(response.toString());
            JsonNode addressNode = root.path("address");
            return addressNode.path("state").asText(null); // null si no existe

        } catch (Exception e) {
            System.err.println("Error en obtenerProvinciaAPI: " + e.getMessage());
            return null;
        }
    }


    private List<String> catalogoCategorias = List.of(
            "vientos fuertes",
            "inundaciones",
            "granizo",
            "nevadas",
            "calor extremo",
            "sequía",
            "derrumbes",
            "actividad volcánica",
            "incendios",
            "contaminación",
            "evento sanitario",
            "derrame",
            "intoxicación masiva"
    );

    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd") // ojo: solo fecha
    );

    private Ubicacion normalizarUbicacion(HechoDTO_IN hechoSinNormalizar) {
        try {
            String latitudNormalizada = hechoSinNormalizar.getLatitud().replace(",", ".");
            String longitudNormalizada = hechoSinNormalizar.getLongitud().replace(",", ".");
            double lat = Double.parseDouble(latitudNormalizada);
            double lon = Double.parseDouble(longitudNormalizada);

            String provincia = obtenerProvinciaAPI(lat, lon);
            return new Ubicacion(lat, lon, provincia != null ? provincia : "Desconocida");
        } catch (Exception e) {
            System.err.println("Coordenadas inválidas en hecho: " + hechoSinNormalizar.getTitulo());
            return new Ubicacion(0.0, 0.0, "Desconocida");
        }
    }


    private String categoriaEnCatalogo(String categoria){
        return catalogoCategorias.stream()
                .filter(cat -> categoria.toLowerCase()
                        .contains(cat.toLowerCase()))
                .findFirst().orElse("Sin categoria");
    }

    private String normalizarCategoria(HechoDTO_IN hechoSinNormalizar){
        return categoriaEnCatalogo(hechoSinNormalizar.getCategoria());
    }

    private static LocalDateTime parse(String dateStr) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                if (formatter.toString().contains("H")) {
                    // formato con hora
                    return LocalDateTime.parse(dateStr, formatter);
                } else {
                    // formato solo fecha → convertir a LocalDate y luego a LocalDateTime
                    LocalDate d = LocalDate.parse(dateStr, formatter);
                    return d.atStartOfDay();
                }
            } catch (DateTimeParseException e) {
                // intento fallido, sigo al siguiente
            }
        }
        throw new IllegalArgumentException("Formato de fecha no soportado: " + dateStr);
    }

    private LocalDateTime normalizarFecha(HechoDTO_IN hechoSinNormalizar){
        return parse(hechoSinNormalizar.getFechaAcontecimiento());
    }


    private Origen normalizaOrigen(String origen) {
        if ("DINAMICA".equals(origen)) {
            return Origen.DINAMICA;
        }
        if ("ESTATICA".equals(origen)) {
            return Origen.ESTATICA;
        }
        return Origen.PROXY;
    }

    private Hecho normalizarHecho(HechoDTO_IN hechoDTO){

        Hecho nuevoHecho = new Hecho(
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                normalizarCategoria(hechoDTO),
                normalizarUbicacion(hechoDTO),
                normalizarFecha(hechoDTO),
                hechoDTO.getEtiqueta(),
                hechoDTO.getArchivosMultimedia()
        );

        System.out.println("el origen es: " + hechoDTO.getOrigen());

        nuevoHecho.getOrigenes().add(normalizaOrigen(hechoDTO.getOrigen()));

        return nuevoHecho;
    }

    public List<Hecho> normalizarHechos(List<HechoDTO_IN> hechosSinNormalizar){
        return hechosSinNormalizar.stream().map(this::normalizarHecho).toList();
    }
}