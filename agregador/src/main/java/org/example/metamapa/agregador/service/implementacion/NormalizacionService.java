package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.models.dtos.HechoDTO;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.Ubicacion;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NormalizacionService {

    /*
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "dd-MM-yyyy HH:mm:ss",
            "dd/MM/yyyy HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd"

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Primero convertimos a LocalDate
            LocalDate fecha = LocalDate.parse(parts[5], formatter);

            // Convertimos LocalDate a LocalDateTime agregando la hora (00:00)
            LocalDateTime fechaHora = fecha.atStartOfDay();
     */

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


    private void normalizarUbicacion(HechoDTO hechoSinNormalizar){
        String latitudNormalizada = hechoSinNormalizar.getLatitud().replace(",", ".");
        hechoSinNormalizar.setLatitud(latitudNormalizada);
        String longitudNormalizada = hechoSinNormalizar.getLongitud().replace(",", ".");
        hechoSinNormalizar.setLongitud(longitudNormalizada);
    }

    public void normalizarUbicaciones(List<HechoDTO> hechosCrudos){
        hechosCrudos.forEach(this::normalizarUbicacion);
    }

    private String categoriaEnCatalogo(String categoria){
        return catalogoCategorias.stream()
                .filter(cat -> categoria.toLowerCase()
                        .contains(cat.toLowerCase()))
                .findFirst().orElse("");
    }

    private void normalizarCategoria(HechoDTO hechoSinNormalizar){
        String newCategory = categoriaEnCatalogo(hechoSinNormalizar.getCategoria());

        if(newCategory.isEmpty()){
            hechoSinNormalizar.setSinCategorizar(true);
        }else{
            hechoSinNormalizar.setCategoria(newCategory);
        }
    }

    private void normalizarCategorias(List<HechoDTO> hechoSinNormalizar){
        hechoSinNormalizar.forEach(this::normalizarCategoria);
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

    private void normalizarFecha(HechoDTO hechoSinNormalizar){
        LocalDateTime ldt = parse(hechoSinNormalizar.getFechaAcontecimiento());
        hechoSinNormalizar.setFechaAcontecimientoPosta(ldt);
    }

    private void normalizarFechas(List<HechoDTO> hechosSinNormalizar){
        hechosSinNormalizar.forEach(this::normalizarFecha);
    }

    private Hecho convertirAHechoPuro(HechoDTO hechoDTO){
        return new Hecho(hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                hechoDTO.getCategoria(),
                new Ubicacion(Double.parseDouble(hechoDTO.getLatitud()),Double.parseDouble(hechoDTO.getLongitud())),
                hechoDTO.getFechaAcontecimientoPosta(), //PREGUNTAR A MARIANO NO SE SI ESTA BIEN YA QUE ESTA EL POSTA Y EL ORIGINAL
                hechoDTO.getEtiqueta()
        );

    }

    public List<Hecho> normalizarHechos(List<HechoDTO> hechosSinNormalizar){

        List<Hecho> hechos = new ArrayList<>();

        //normalizo los dto
        normalizarUbicaciones(hechosSinNormalizar);
        normalizarCategorias(hechosSinNormalizar);
        normalizarFechas(hechosSinNormalizar);

        //pasar a hechoPosta
        return hechosSinNormalizar.stream().map(this::convertirAHechoPuro).toList();

    }
}