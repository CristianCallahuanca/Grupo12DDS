package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.IFiltradorService;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HechoService implements IHechoService {

    private static final Logger log = LoggerFactory.getLogger(HechoService.class);

    private final IHechosRepository repositorioHechos;
    private final IFiltradorService filtradorService;

    /*
       ================ BÚSQUEDA Y FILTRADO ======================
    */

    @Override
    @Transactional(readOnly = true)
    public List<HechoOutputDTO> buscarTodosLosHechos(List<CriterioRequest> criterios) {
        List<CondicionDeFiltrado> condiciones = new ArrayList<>();

        if (criterios != null && !criterios.isEmpty()) {
            condiciones.addAll(
                    criterios.stream()
                            .map(StringAObjetos::criterioFactory)
                            .toList()
            );
        }

        log.debug("Se crearon {} condiciones de filtrado", condiciones.size());
        List<Hecho> hechos = this.filtrarHechos(condiciones);
        log.debug("Se obtuvieron {} hechos desde la base de datos", hechos.size());

        return hechoADTOOuts(hechos);
    }

    @Override
    public List<Hecho> filtrarHechos(List<CondicionDeFiltrado> condiciones) {
        if (condiciones == null || condiciones.isEmpty())
            return repositorioHechos.findAll();

        return filtradorService.filtrarHechosDataBase(condiciones);
    }

    public long contarTodos() {
        return repositorioHechos.count();
    }
    /*
       ================== EDICIÓN CONTROLADA =====================
    */

    @Override
    public boolean editarHechoContribuyente(Long id, Map<String, Object> cambios) {
        Optional<Hecho> opt = repositorioHechos.findById(id);
        if (opt.isEmpty()) return false;

        Hecho hecho = opt.get();


        if (!puedeEditar(hecho)) {
            log.warn("Intento de edición no permitido para hecho {}", id);
            return false;
        }

        cambios.forEach((campo, valor) -> {
            switch (campo) {
                case "titulo" -> hecho.setTitulo((String) valor);
                case "descripcion" -> hecho.setDescripcion((String) valor);
                case "categoria" -> hecho.setCategoria((String) valor);
                case "etiqueta" -> hecho.setEtiqueta((String) valor);
            }
        });

        repositorioHechos.save(hecho);
        log.info("Hecho {} editado correctamente por contribuyente", id);
        return true;
    }

    private boolean puedeEditar(Hecho hecho) {
        boolean esDinamica = hecho.getOrigenes() != null && hecho.getOrigenes().contains(Origen.DINAMICA);
        boolean dentroDePlazo = hecho.getFechaCarga() != null &&
                ChronoUnit.DAYS.between(hecho.getFechaCarga(), LocalDateTime.now()) <= 7;
        return esDinamica && dentroDePlazo;
    }

    /*
       ===================== CONVERSORES =========================
     */

    @Override
    public List<HechoOutputDTO> hechoADTOOuts(List<Hecho> hechos) {
        return hechos.stream().map(this::hechoADTOOut).collect(Collectors.toList());
    }

    private HechoOutputDTO hechoADTOOut(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setLatitud(String.valueOf(hecho.getUbicacion().getLatitud()));
        dto.setLongitud(String.valueOf(hecho.getUbicacion().getLongitud()));
        dto.setFechaAcontecimiento(
                hecho.getFechaAcontecimiento() != null
                        ? hecho.getFechaAcontecimiento().toString()
                        : null
        ); //TODO: CURAR ESTO
        dto.setEtiqueta(hecho.getEtiqueta());
        dto.setArchivosMultimedia(hecho.getArchivosMultimedia());

        if (hecho.getContribuyente() != null) {
            ContribuyenteRegistrado c = hecho.getContribuyente();
            dto.setNombre_contribuyente(c.getNombre());
            dto.setApellido_contribuyente(c.getApellido());
        }

        return dto;
    }

    /*
       =================== QUERY → CRITERIOS =====================
    */

    @Override
    public List<CriterioRequest> convertirQueryParamsACriterios(Map<String, String> queryParams) {
        List<CriterioRequest> criterios = new ArrayList<>();

        for (var entry : queryParams.entrySet()) {
            String clave = entry.getKey();
            String valor = entry.getValue();
            if (valor == null || valor.trim().isEmpty()) continue;

            CriterioRequest criterio = new CriterioRequest();

            switch (clave) {
                case "tituloBuscado" -> {
                    criterio.setTipo("porTitulo");
                    criterio.setParams(Map.of("tituloBuscado", valor));
                }
                case "categoriaDeseada" -> {
                    criterio.setTipo("porCategoria");
                    criterio.setParams(Map.of("categoriaDeseada", valor));
                }
                case "fraseClave" -> {
                    criterio.setTipo("porDescripcion");
                    criterio.setParams(Map.of("fraseClave", valor));
                }
                case "etiquetaDeseada" -> {
                    criterio.setTipo("porEtiqueta");
                    criterio.setParams(Map.of("etiquetaDeseada", valor));
                }
                case "origenDeseado" -> {
                    criterio.setTipo("porOrigen");
                    criterio.setParams(Map.of("origenDeseado", valor.toUpperCase()));
                }
                case "estadoDeseado" -> {
                    criterio.setTipo("porEstado");
                    criterio.setParams(Map.of("estadoDeseado", valor.toUpperCase()));
                }
                case "sinCategorizar" -> {
                    criterio.setTipo("porSinCategorizar");
                    criterio.setParams(Map.of("sinCategorizar", valor));
                }
                case "idBuscado" -> {
                    criterio.setTipo("porIDHecho");
                    criterio.setParams(Map.of("idBuscado", valor));
                }
                case "latitud", "longitud" -> {
                    // se manejan juntos si están ambos presentes
                    if (queryParams.containsKey("latitud") && queryParams.containsKey("longitud")) {
                        criterio.setTipo("porUbicacion");
                        criterio.setParams(Map.of(
                                "latitud", queryParams.get("latitud"),
                                "longitud", queryParams.get("longitud")
                        ));
                    }
                }
                case "desdeCarga" -> {
                    criterio.setTipo("porFechaCargaDesde");
                    criterio.setParams(Map.of("desde", valor));
                }
                case "hastaCarga" -> {
                    criterio.setTipo("porFechaCargaHasta");
                    criterio.setParams(Map.of("hasta", valor));
                }
                case "desdeAcontecimiento" -> {
                    criterio.setTipo("porFechaAcontecimientoDesde");
                    criterio.setParams(Map.of("desde", valor));
                }
                case "hastaAcontecimiento" -> {
                    criterio.setTipo("porFechaAcontecimientoHasta");
                    criterio.setParams(Map.of("hasta", valor));
                }
            }

            if (criterio.getTipo() != null) criterios.add(criterio);
        }

        log.debug("→ Generados {} criterios desde query params", criterios.size());
        return criterios;
    }


}
