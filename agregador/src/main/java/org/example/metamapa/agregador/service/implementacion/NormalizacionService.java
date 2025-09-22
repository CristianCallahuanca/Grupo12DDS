package org.example.metamapa.agregador.service.implementacion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.Hecho;
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
import java.util.List;

@Service
public class NormalizacionService implements INormalizacionService {

    //Atte GPT:
    public static String obtenerProvinciaAPI(double lat, double lon) {
        String urlString = String.format(
                "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json",
                lat, lon
        );

        try {
            // Hacer la request
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "JavaApp"); // Nominatim requiere un User-Agent

            // Leer la respuesta
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parsear el JSON con Jackson
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());

            // Navegar hasta "address" -> "state"
            JsonNode addressNode = root.path("address");
            return addressNode.path("state").asText();

        } catch (Exception e) {
            e.printStackTrace();
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

    private Ubicacion normalizarUbicacion(HechoDTO_IN hechoSinNormalizar){
        String latitudNormalizada = hechoSinNormalizar.getLatitud().replace(",", ".");
        hechoSinNormalizar.setLatitud(latitudNormalizada);
        String longitudNormalizada = hechoSinNormalizar.getLongitud().replace(",", ".");
        hechoSinNormalizar.setLongitud(longitudNormalizada);

        double latitud = Double.parseDouble(latitudNormalizada);
        double longitud = Double.parseDouble(longitudNormalizada);

        return new Ubicacion(Double.parseDouble(latitudNormalizada), Double.parseDouble(longitudNormalizada), obtenerProvinciaAPI(latitud, longitud));
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

    private Hecho normalizarHecho(HechoDTO_IN hechoDTO){
        return new Hecho(
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                normalizarCategoria(hechoDTO),
                normalizarUbicacion(hechoDTO),
                normalizarFecha(hechoDTO),
                hechoDTO.getEtiqueta(),
                hechoDTO.getArchivosMultimedia()
        );
    }

    public List<Hecho> normalizarHechos(List<HechoDTO_IN> hechosSinNormalizar){
        return hechosSinNormalizar.stream().map(this::normalizarHecho).toList();
    }
}