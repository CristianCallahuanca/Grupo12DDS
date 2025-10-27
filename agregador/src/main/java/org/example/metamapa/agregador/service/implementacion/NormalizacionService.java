package org.example.metamapa.agregador.service.implementacion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.infraestructura.ProvinciaLocator;
import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.*;
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
    private final Map<String, OrigenReal> cacheOrigenes = new HashMap<>();
    private final IContribuyenteRepository contribuyenteRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public NormalizacionService(IContribuyenteRepository contribuyenteRepository) {
        this.contribuyenteRepository = contribuyenteRepository;
    }

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

        Hecho h = new Hecho(
                dto.getTitulo(),
                dto.getDescripcion(),
                normalizarCategoria(dto),
                normalizarUbicacion(dto),
                normalizarFecha(dto),
                dto.getEtiqueta(),
                dto.getArchivosMultimedia()
        );

        TipoFuente tipoFuente = normalizaOrigen(dto.getTipoFuente());
        h.setTipoFuente(tipoFuente);
        log.debug("   • TipoFuente asignado: {}", tipoFuente);

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
            log.debug("Contribuyente asignado: {}", contribuyenteId);
        } else {
            h.setContribuyente(null);
            log.debug("Sin contribuyente (no aplica para tipo {})", tipoFuente);
        }

        if (dto.getOrigen() != null && !dto.getOrigen().isBlank()) {
            String nombre = dto.getOrigen().trim();
            log.debug("Analizando origen: '{}'", nombre);

            OrigenReal origen = cacheOrigenes.get(nombre);
            if (origen != null) {
                log.debug("Origen encontrado en cache: '{}'", nombre);
            } else {
                // Buscar en BD
                origen = origenRealRepository.findByNombreIgnoreCase(nombre).orElse(null);

                if (origen != null) {
                    log.debug("Origen encontrado en BD: '{}'", nombre);
                } else {
                    log.debug("Origen no encontrado, creando nuevo: '{}'", nombre);
                    try {
                        origen = origenRealRepository.save(new OrigenReal(null, nombre, tipoFuente));
                        log.debug("Origen creado y guardado en BD: '{}'", nombre);
                    } catch (DataIntegrityViolationException e) {
                        log.warn("Duplicado detectado al guardar '{}', recuperando existente", nombre);
                        origen = origenRealRepository.findByNombreIgnoreCase(nombre)
                                .orElseThrow(() -> new RuntimeException("Origen duplicado no encontrado tras excepción"));
                    }
                }

                cacheOrigenes.put(nombre, origen);
                log.debug("Origen agregado a cache: '{}'", nombre);
            }

            h.setOrigenReal(origen);
            log.info("Origen asignado al hecho '{}': {}", dto.getTitulo(), origen.getNombre());
        } else {
            log.warn("Hecho '{}' vino sin origen — no se asignará OrigenReal", dto.getTitulo());
        }

        return h;
    }



    public List<Hecho> normalizarHechos(List<HechoDTO_IN> hechos) {
        cacheOrigenes.clear();

        log.info("Precargando orígenes reales existentes en cache...");
        origenRealRepository.findAll().forEach(o -> cacheOrigenes.put(o.getNombre(), o));
        log.info("{} orígenes reales precargados", cacheOrigenes.size());

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

            if ((i + 1) % 1000 == 0) {
                log.info("Progreso: {} / {} hechos normalizados", (i + 1), total);
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
