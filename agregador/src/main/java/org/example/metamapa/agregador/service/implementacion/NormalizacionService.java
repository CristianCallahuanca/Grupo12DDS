package org.example.metamapa.agregador.service.implementacion;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.infraestructura.ProvinciaLocator;
import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.Origen;
import org.example.metamapa.agregador.models.entidades.Ubicacion;
import org.example.metamapa.agregador.service.INormalizacionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


@Service
@Slf4j
public class NormalizacionService implements INormalizacionService {


    private static final Map<String, List<String>> catalogoCategorias = Map.ofEntries(
            Map.entry("vientos fuertes", List.of("viento", "temporal", "tormenta", "ráfaga", "vendaval")),
            Map.entry("inundaciones", List.of("inundación", "anegamiento", "crecida", "desborde", "lluvia")),
            Map.entry("granizo", List.of("granizo", "piedra")),
            Map.entry("nevadas", List.of("nieve", "nevada")),
            Map.entry("calor extremo", List.of("calor", "ola de calor", "temperatura alta", "térmico")),
            Map.entry("sequía", List.of("sequía", "falta de agua", "escasez hídrica", "árido")),
            Map.entry("derrumbes", List.of("derrumbe", "deslizamiento", "alud", "corte de ruta")),
            Map.entry("actividad volcánica", List.of("volcán", "erupción")),
            Map.entry("incendios", List.of("incendio", "fuego", "quema", "forestal")),
            Map.entry("contaminación", List.of("contaminación", "vertido", "basura", "residuos")),
            Map.entry("evento sanitario", List.of("enfermedad", "brote", "epidemia", "pandemia", "virus")),
            Map.entry("derrame", List.of("derrame", "petróleo", "químico", "aceite")),
            Map.entry("intoxicación masiva", List.of("intoxicación", "alimento", "veneno"))
    );

    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );


    private Ubicacion normalizarUbicacion(HechoDTO_IN dto) {
        try {
            double lat = Double.parseDouble(dto.getLatitud().replace(",", "."));
            double lon = Double.parseDouble(dto.getLongitud().replace(",", "."));
            String provincia = ProvinciaLocator.obtenerProvincia(lat, lon);
            return new Ubicacion(lat, lon, provincia);
        } catch (Exception e) {
            log.warn("Coordenadas inválidas en hecho '{}'", dto.getTitulo());
            return new Ubicacion(0.0, 0.0, null);
        }
    }


    private String normalizarCategoria(HechoDTO_IN dto) {
        String textoBase = (dto.getCategoria() + " " + dto.getDescripcion());
        return detectarCategoria(textoBase);
    }


    public String normalizarCategoriaDesdeTexto(String titulo, String descripcion) {
        String textoBase = (titulo == null ? "" : titulo) + " " + (descripcion == null ? "" : descripcion);
        return detectarCategoria(textoBase);
    }


    private String detectarCategoria(String textoBase) {
        if (textoBase == null || textoBase.isBlank()) return "Sin categoria";

        String texto = textoBase.toLowerCase().replaceAll("[^a-záéíóúñü ]", "");

        return catalogoCategorias.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(texto::contains))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("Sin categoria");
    }


    private static LocalDateTime parse(String dateStr) {
        for (DateTimeFormatter f : FORMATTERS) {
            try {
                return dateStr.contains(":")
                        ? LocalDateTime.parse(dateStr, f)
                        : LocalDate.parse(dateStr, f).atStartOfDay();
            } catch (DateTimeParseException ignored) {}
        }
        log.warn("Formato de fecha no soportado: {}", dateStr);
        return LocalDateTime.now();
    }

    private LocalDateTime normalizarFecha(HechoDTO_IN dto) {
        return parse(dto.getFechaAcontecimiento());
    }

    private Origen normalizaOrigen(String tipoFuente) {
        if ("DINAMICA".equalsIgnoreCase(tipoFuente)) return Origen.DINAMICA;
        if ("ESTATICA".equalsIgnoreCase(tipoFuente)) return Origen.ESTATICA;
        return Origen.PROXY;
    }


    public Hecho normalizarHecho(HechoDTO_IN dto) {

        Hecho h = new Hecho(
                dto.getTitulo(),
                dto.getDescripcion(),
                normalizarCategoria(dto),
                normalizarUbicacion(dto),
                normalizarFecha(dto),
                dto.getEtiqueta(),
                dto.getArchivosMultimedia()
        );
        h.getOrigenes().add(normalizaOrigen(dto.getTipoFuente()));
        h.setOrigenReal(dto.getOrigen());

        return h;
    }


    public List<Hecho> normalizarHechos(List<HechoDTO_IN> hechos) {
        List<Hecho> lista = new ArrayList<>();
        int total = hechos.size();
        log.info("Iniciando normalización de {} hechos...", total);

        for (int i = 0; i < total; i++) {
            HechoDTO_IN dto = hechos.get(i);
            try {
                lista.add(normalizarHecho(dto));
            } catch (Exception e) {
                log.warn("Error normalizando '{}': {}", dto.getTitulo(), e.getMessage());
            }

            // Log de progreso cada 1000 hechos
            if ((i + 1) % 1000 == 0) {
                log.info("→ Progreso: {} / {} hechos normalizados", (i + 1), total);
            }
        }

        log.info("Normalización completada. Total procesados: {}", lista.size());
        return lista;
    }


}
