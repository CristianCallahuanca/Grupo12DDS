package org.example.metamapa.agregador.service.implementacion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.infraestructura.ProvinciaLocator;
import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.*;
import org.example.metamapa.agregador.models.repositorios.ICategoriaRepository;
import org.example.metamapa.agregador.models.repositorios.IContribuyenteRepository;
import org.example.metamapa.agregador.models.repositorios.IOrigenRealRepository;
import org.example.metamapa.agregador.service.INormalizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


@Service
@Slf4j
public class NormalizacionService implements INormalizacionService {

    @Autowired
    private IOrigenRealRepository origenRealRepository;
    @Autowired
    private IContribuyenteRepository contribuyenteRepository;
    @Autowired
    private CatalogoCategoriasService catalogoCategoriasService;

    @PersistenceContext
    private EntityManager entityManager;

    private final Map<String, OrigenReal> cacheOrigenes = new HashMap<>();

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

    private Categoria detectarCategoriaDesdeTexto(String textoBase) {
        if (textoBase == null || textoBase.isBlank()) return null;

        String texto = textoBase.toLowerCase().replaceAll("[^a-záéíóúñü ]", "");

        Map<String, List<String>> catalogo = catalogoCategoriasService.obtenerSinonimosPorCategoria();

        return catalogo.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(texto::contains))
                .findFirst()
                .map(entry -> catalogoCategoriasService.obtenerCategoriaPorNombre(entry.getKey()))
                .orElse(null);
    }


    private Categoria normalizarCategoria(HechoDTO_IN dto) {
        String textoBase = (dto.getCategoria() + " " + dto.getDescripcion());
        return detectarCategoriaDesdeTexto(textoBase);
    }

    public Categoria normalizarCategoriaDesdeTexto(String titulo, String descripcion) {
        String textoBase = (titulo == null ? "" : titulo) + " " + (descripcion == null ? "" : descripcion);
        return detectarCategoriaDesdeTexto(textoBase);
    }

    private static LocalDateTime parse(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;

        for (DateTimeFormatter f : FORMATTERS) {
            try {
                return dateStr.contains(":")
                        ? LocalDateTime.parse(dateStr.trim(), f)
                        : LocalDate.parse(dateStr.trim(), f).atStartOfDay();
            } catch (DateTimeParseException ignored) {}
        }

        log.warn("Formato de fecha no soportado: {}", dateStr);
        return null;
    }

    private LocalDateTime normalizarFecha(HechoDTO_IN dto) {
        return parse(dto.getFechaAcontecimiento());
    }

    private TipoFuente normalizaOrigen(String tipoFuente) {
        if (tipoFuente == null) return TipoFuente.ESTATICA;
        return switch (tipoFuente.toUpperCase()) {
            case "DINAMICA" -> TipoFuente.DINAMICA;
            case "ESTATICA" -> TipoFuente.ESTATICA;
            case "DEMO" -> TipoFuente.DEMO;
            case "METAMAPA" -> TipoFuente.METAMAPA;
            default -> TipoFuente.ESTATICA;
        };
    }



    public Hecho normalizarHecho(HechoDTO_IN dto) {
        log.info("→ Normalizando hecho: '{}'", dto.getTitulo());

        // Normalizamos campos simples
        Ubicacion ubicacion = normalizarUbicacion(dto);
        LocalDateTime fecha = normalizarFecha(dto);
        Categoria categoria = normalizarCategoria(dto);

        Hecho h = new Hecho(
                dto.getTitulo(),
                dto.getDescripcion(),
                categoria,
                ubicacion,
                fecha,
                dto.getEtiqueta(),
                dto.getArchivosMultimedia()
        );

        TipoFuente tipoFuente = normalizaOrigen(dto.getTipoFuente());
        h.setTipoFuente(tipoFuente);
        log.debug("   • TipoFuente asignado: {}", tipoFuente);

        // Contribuyente
        if (tipoFuente == TipoFuente.DINAMICA &&
                dto.getContribuyenteID() != null && !dto.getContribuyenteID().isBlank()) {

            String contribuyenteId = dto.getContribuyenteID();

            ContribuyenteRegistrado contribuyente = contribuyenteRepository.findById(contribuyenteId)
                    .orElseGet(() -> {
                        log.debug("Nuevo contribuyente detectado: {}", contribuyenteId);
                        ContribuyenteRegistrado nuevo = new ContribuyenteRegistrado();
                        nuevo.setId(contribuyenteId);
                        return entityManager.merge(nuevo);
                    });

            h.setContribuyente(contribuyente);
        }

        // Origen real
        if (dto.getOrigen() != null && !dto.getOrigen().isBlank()) {
            String nombre = dto.getOrigen().trim();

            OrigenReal origen = cacheOrigenes.computeIfAbsent(nombre, key -> {
                return origenRealRepository.findByNombreIgnoreCase(key)
                        .orElseGet(() -> {
                            try {
                                return origenRealRepository.save(new OrigenReal(null, key, tipoFuente));
                            } catch (DataIntegrityViolationException e) {
                                log.warn("Duplicado detectado al guardar '{}', recuperando existente", key);
                                return origenRealRepository.findByNombreIgnoreCase(key)
                                        .orElseThrow(() -> new RuntimeException("Origen duplicado no encontrado"));
                            }
                        });
            });

            h.setOrigenReal(origen);
        }

        return h;
    }

    public List<Hecho> normalizarHechos(List<HechoDTO_IN> hechos) {
        cacheOrigenes.clear();
        origenRealRepository.findAll().forEach(o -> cacheOrigenes.put(o.getNombre(), o));

        List<Hecho> lista = new ArrayList<>();
        for (HechoDTO_IN dto : hechos) {
            try {
                lista.add(normalizarHecho(dto));
            } catch (Exception e) {
                log.warn("Error normalizando '{}': {}", dto.getTitulo(), e.getMessage());
            }
        }

        log.info("Normalización completada. Total procesados: {}", lista.size());
        return lista;
    }

    public OrigenReal obtenerOCrear(String nombre, TipoFuente tipoFuente) {
        return origenRealRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    try {
                        return origenRealRepository.save(new OrigenReal(null, nombre, tipoFuente));
                    } catch (DataIntegrityViolationException e) {
                        log.warn("OrigenReal duplicado detectado para '{}', reutilizando existente", nombre);
                        return origenRealRepository.findByNombre(nombre)
                                .orElseThrow(() -> new RuntimeException("Origen duplicado no encontrado"));
                    }
                });
    }
}


