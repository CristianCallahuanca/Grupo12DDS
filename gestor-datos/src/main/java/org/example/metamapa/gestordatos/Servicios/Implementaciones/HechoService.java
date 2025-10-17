package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HechoService implements IHechoService {

    private final IHechosRepository repositorioHechos;
    private final FiltradorService filtradorService;

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
        Set<String> criteriosProcesados = new HashSet<>(); // Para evitar duplicados

        for (var entry : queryParams.entrySet()) {
            String clave = entry.getKey();
            String valor = entry.getValue();
            if (valor == null || valor.trim().isEmpty()) continue;

            CriterioRequest criterio = new CriterioRequest();

            switch (clave) {
                case "coleccionId" -> {
                    criterio.setTipo("porColeccion");
                    criterio.setParams(Map.of("coleccionBuscada", valor));
                }
                case "titulo" -> {
                    criterio.setTipo("porTitulo");
                    criterio.setParams(Map.of("tituloBuscado", valor));
                }
                case "categoria" -> {
                    criterio.setTipo("porCategoria");
                    criterio.setParams(Map.of("categoriaDeseada", valor));
                }
                case "descripcion" -> { // Agregué este caso que te faltaba
                    criterio.setTipo("porDescripcion");
                    criterio.setParams(Map.of("fraseClave", valor));
                }
                case "fraseClave" -> {
                    criterio.setTipo("porDescripcion");
                    criterio.setParams(Map.of("fraseClave", valor));
                }
                case "etiquetaDeseada" -> {
                    criterio.setTipo("porEtiqueta");
                    criterio.setParams(Map.of("etiquetaDeseada", valor));
                }
                case "origen" -> {
                    criterio.setTipo("porOrigen");
                    criterio.setParams(Map.of("unOrigen", valor.toUpperCase()));
                }
                case "estadoDeseado" -> {
                    criterio.setTipo("porEstado");
                    criterio.setParams(Map.of("estadoHecho", valor.toUpperCase()));
                }
                case "sinCategorizar" -> {
                    criterio.setTipo("porSinCategorizar");
                    criterio.setParams(Map.of("sinCategorizar", valor));
                }
                case "idBuscado" -> {
                    criterio.setTipo("porIDHecho");
                    criterio.setParams(Map.of("idBuscado", valor));
                }
                case "contieneMultimedia" -> { // Agregué este caso que te faltaba
                    criterio.setTipo("contieneMultimedia");
                    criterio.setParams(Map.of("multimedia", valor));
                }
                case "desdeCarga" -> {
                    criterio.setTipo("porFechaCargaDesde");
                    criterio.setParams(Map.of("desde", valor));
                    System.out.println("desdeCarga: " + valor);
                }
                case "hastaCarga" -> {
                    criterio.setTipo("porFechaCargaHasta");
                    criterio.setParams(Map.of("hasta", valor));
                    System.out.println("hastaCarga: " + valor);
                }
                case "desdeAcontecimiento" -> {
                    criterio.setTipo("porFechaAcontecimientoDesde");
                    criterio.setParams(Map.of("desde", valor));
                    System.out.println("desdeAcontecimiento: " + valor);
                }
                case "hastaAcontecimiento" -> {
                    criterio.setTipo("porFechaAcontecimientoHasta");
                    criterio.setParams(Map.of("hasta", valor));
                    System.out.println("hastaAcontecimiento: " + valor);
                }

            }

            if (criterio.getTipo() != null && !criteriosProcesados.contains(criterio.getTipo())) {
                criterios.add(criterio);
                criteriosProcesados.add(criterio.getTipo());
            }
        }

        // MANEJAR CRITERIOS COMPUESTOS FUERA DEL LOOP

        // 1. PorAreaVisible (solo una vez)
        if (queryParams.containsKey("norte") && queryParams.containsKey("sur") &&
                queryParams.containsKey("este") && queryParams.containsKey("oeste")) {

            String norte = queryParams.get("norte");
            String sur = queryParams.get("sur");
            String este = queryParams.get("este");
            String oeste = queryParams.get("oeste");

            if (norte != null && !norte.trim().isEmpty() &&
                    sur != null && !sur.trim().isEmpty() &&
                    este != null && !este.trim().isEmpty() &&
                    oeste != null && !oeste.trim().isEmpty()) {

                CriterioRequest criterioArea = new CriterioRequest();
                criterioArea.setTipo("porAreaVisible");
                criterioArea.setParams(Map.of(
                        "norte", norte,
                        "sur", sur,
                        "este", este,
                        "oeste", oeste
                ));
                criterios.add(criterioArea);
            }
        }

        log.debug("→ Generados {} criterios desde query params", criterios.size());
        return criterios;
    }


}
