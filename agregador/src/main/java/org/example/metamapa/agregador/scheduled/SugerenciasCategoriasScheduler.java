package org.example.metamapa.agregador.scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SugerenciasCategoriasScheduler {

    @Autowired
    private IRepositorioHechos hechoRepo;

    // Umbral mínimo de aparición de una palabra (15% de los hechos sin categoría)
    private static final double UMBRAL_PORCENTAJE = 0.15;

    // Stopwords básicas (se pueden ampliar)
    private static final Set<String> STOPWORDS = Set.of(
            "para", "este", "esta", "esas", "ellos", "ellas", "donde", "que", "con", "los", "las",
            "por", "del", "una", "uno", "unos", "unas", "muy", "pero", "solo", "entre", "hacia",
            "sobre", "como", "cuando", "aquel", "aquella", "aquí", "allí", "desde", "hasta",
            "porque", "según", "cada", "todo", "toda", "todos", "todas"
    );

    @Scheduled(cron = "0 0 4 */3 * *") // cada 3 días a las 04:00
    public void generarSugerencias() {
        log.info("Iniciando generación de sugerencias de categorías...");

        List<Hecho> hechosSinCategoria = hechoRepo.findByCategoriaIsNull();

        if (hechosSinCategoria.isEmpty()) {
            log.info("No hay hechos sin categoría. No se generan sugerencias.");
            return;
        }

        long total = hechosSinCategoria.size();
        Map<String, Long> frecuencia = new HashMap<>();

        // Recorremos todos los hechos sin categoría
        for (Hecho h : hechosSinCategoria) {
            String texto = (Optional.ofNullable(h.getTitulo()).orElse("") + " " +
                    Optional.ofNullable(h.getDescripcion()).orElse(""))
                    .toLowerCase()
                    .replaceAll("[^a-záéíóúñü ]", " ");

            Arrays.stream(texto.split("\\s+"))
                    .filter(p -> p.length() > 3)
                    .filter(p -> !STOPWORDS.contains(p))
                    .forEach(p -> frecuencia.merge(p, 1L, Long::sum));
        }

        // Filtrar las palabras más frecuentes (≥15% de los hechos sin categoría)
        List<Map<String, Object>> sugerencias = frecuencia.entrySet().stream()
                .filter(e -> (double) e.getValue() / total >= UMBRAL_PORCENTAJE)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("palabra", e.getKey());
                    m.put("frecuencia", e.getValue());
                    return m;
                })
                .collect(Collectors.toList());

        if (sugerencias.isEmpty()) {
            log.info("No se encontraron palabras que superen el umbral de frecuencia ({})", UMBRAL_PORCENTAJE);
            return;
        }

        // Crear carpeta data/ si no existe
        try {
            Files.createDirectories(Path.of("data"));
        } catch (IOException e) {
            log.error("Error creando carpeta data/: {}", e.getMessage());
        }

        // Guardar sugerencias en JSON
        Path rutaArchivo = Path.of("data/sugerencias_categorias.json");
        try {
            Files.writeString(
                    rutaArchivo,
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(Map.of(
                            "fecha", LocalDate.now().toString(),
                            "total_hechos_analizados", total,
                            "palabras_sugeridas", sugerencias
                    ))
            );
            log.info("Archivo de sugerencias generado en {} con {} palabras candidatas.",
                    rutaArchivo.toAbsolutePath(), sugerencias.size());
        } catch (IOException e) {
            log.error("Error generando archivo de sugerencias: {}", e.getMessage());
        }
    }
}
