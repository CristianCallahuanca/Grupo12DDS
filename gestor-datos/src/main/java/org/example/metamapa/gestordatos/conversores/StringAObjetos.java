package org.example.metamapa.gestordatos.conversores;


import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.*;
import org.example.metamapa.gestordatos.models.Consenso.Absoluto;
import org.example.metamapa.gestordatos.models.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.Consenso.MayoriaSimple;
import org.example.metamapa.gestordatos.models.Consenso.MultiplesMenciones;
import org.example.metamapa.gestordatos.models.ModosNavegacion.Curada;
import org.example.metamapa.gestordatos.models.ModosNavegacion.Irrestricta;
import org.example.metamapa.gestordatos.models.ModosNavegacion.ModoNavegacion;
import org.example.metamapa.gestordatos.models.entidades.Ubicacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.TipoFuente;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class StringAObjetos {

    public static ModoNavegacion modoNavegacionFactory(String modoNavegacion) {
        if (modoNavegacion == null)
            throw new IllegalArgumentException("El modo de navegación no puede ser nulo");

        return switch (modoNavegacion.toLowerCase()) {
            case "curada" -> new Curada();
            case "irrestricta" -> new Irrestricta();
            default -> throw new IllegalArgumentException("Modo no válido: " + modoNavegacion +
                    "Los modos válidos son: curada, irrestricta.");
        };
    }


    public static CondicionDeFiltrado criterioFactory(CriterioRequest request) {
        String tipo = request.getTipo().toLowerCase();
        Map<String, String> params = request.getParams();

        return switch (tipo) {
            case "porcoleccion" -> new PorColeccion(params.get("coleccionBuscada"));
            case "portitulo" -> new PorTitulo(params.get("tituloBuscado"));
            case "porcategoria" -> new PorCategoria(params.get("categoriaDeseada"));
            case "pordescripcion" -> new PorDescripcion(params.get("fraseClave"));
            case "poretiqueta" -> new PorEtiqueta(params.get("etiquetaDeseada"));
            case "portipofuente" -> new PorTipoFuente(TipoFuente.valueOf(params.get("unTipoFuente").toUpperCase()));
            case "pororigenreal" -> new PorOrigenReal(params.get("origenReal"));
            case "porubicacion" -> new PorUbicacion(
                    new Ubicacion(
                            Double.parseDouble(params.get("latitud")),
                            Double.parseDouble(params.get("longitud"))
                    )
            );
            case "contienemultimedia" -> new ContieneMultimedia(Boolean.parseBoolean(params.get("multimedia")));
            case "porfechacargadesde" -> new PorFechaCargaDesde(LocalDateTime.parse(params.get("desde")));
            case "porfechacargahasta" -> new PorFechaCargaHasta(LocalDateTime.parse(params.get("hasta")));
            case "porfechaacontecimientodesde" -> new PorFechaAcontecimientoDesde(LocalDateTime.parse(params.get("desde")));
            case "porfechaacontecimientohasta" -> new PorFechaAcontecimientoHasta(LocalDateTime.parse(params.get("hasta")));
            case "porestado" -> new PorEstado(EstadoHecho.valueOf(params.get("estadoHecho").toUpperCase()));
            case "porsincategorizar" -> new PorSinCategorizar(Boolean.parseBoolean(params.get("sinCategorizar")));
            case "poridcontribuyente" -> new PorIdContribuyente(Long.valueOf(params.get("idBuscado")));
            case "poridhecho" -> new PorIDHecho(Long.parseLong(params.get("idBuscado")));
            case "porareavisible" -> new PorAreaVisible(
                    Double.parseDouble(params.get("norte")),
                    Double.parseDouble(params.get("sur")),
                    Double.parseDouble(params.get("este")),
                    Double.parseDouble(params.get("oeste"))
            );
            default -> throw new IllegalArgumentException("Tipo de criterio no válido: " + tipo);
        };
    }

    public static AlgoritmoConsenso algoritmoConsensoFactory(String algoritmo) {
        if (algoritmo == null || algoritmo.isBlank())
            throw new IllegalArgumentException("El algoritmo de consenso no puede ser nulo ni vacío.");

        return switch (algoritmo.toLowerCase()) {
            case "mayoriasimple" -> new MayoriaSimple();
            case "absoluto" -> new Absoluto();
            case "multiplesmenciones" -> new MultiplesMenciones();
            default -> throw new IllegalArgumentException("Algoritmo no válido: " + algoritmo +
                    "Valores válidos: mayoriasimple, absoluto, multiplesmenciones.");
        };
    }



    public static List<CriterioRequest> convertirQueryParamsACriterios(Map<String, String> queryParams) {
        List<CriterioRequest> criterios = new ArrayList<>();
        Set<String> criteriosProcesados = new HashSet<>(); // Evita duplicados

        for (var entry : queryParams.entrySet()) {
            String clave = entry.getKey();
            String valor = entry.getValue();
            if (valor == null || valor.trim().isEmpty()) continue;

            CriterioRequest criterio = new CriterioRequest();

            switch (clave) {

                // === FILTROS BÁSICOS ===
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
                case "descripcion", "fraseClave" -> {
                    criterio.setTipo("porDescripcion");
                    criterio.setParams(Map.of("fraseClave", valor));
                }
                case "etiquetaDeseada" -> {
                    criterio.setTipo("porEtiqueta");
                    criterio.setParams(Map.of("etiquetaDeseada", valor));
                }

                // === FILTROS DE ORIGEN / FUENTE ===
                case "tipoFuente" -> {
                    if (valor.equalsIgnoreCase("DINAMICA") ||
                            valor.equalsIgnoreCase("ESTATICA") ||
                            valor.equalsIgnoreCase("DEMO") ||
                            valor.equalsIgnoreCase("METAMAPA")) {

                        criterio.setTipo("porTipoFuente");
                        criterio.setParams(Map.of("unTipoFuente", valor.toUpperCase()));
                    }
                }

                case "origenReal" -> {
                    criterio.setTipo("porOrigenReal");
                    criterio.setParams(Map.of("origenReal", valor));
                }

                // === FILTROS DE ESTADO Y ATRIBUTOS ===
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
                case "idContribuyente" -> {
                    criterio.setTipo("porIdContribuyente");
                    criterio.setParams(Map.of("idBuscado", valor));
                }
                case "contieneMultimedia" -> {
                    criterio.setTipo("contieneMultimedia");
                    criterio.setParams(Map.of("multimedia", valor));
                }

                // === FILTROS TEMPORALES ===
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

            if (criterio.getTipo() != null && !criteriosProcesados.contains(criterio.getTipo())) {
                criterios.add(criterio);
                criteriosProcesados.add(criterio.getTipo());
            }
        }

        // === FILTRO COMPUESTO: ÁREA VISIBLE ===
        if (queryParams.containsKey("norte") && queryParams.containsKey("sur") &&
                queryParams.containsKey("este") && queryParams.containsKey("oeste")) {

            String norte = queryParams.get("norte");
            String sur = queryParams.get("sur");
            String este = queryParams.get("este");
            String oeste = queryParams.get("oeste");

            if (norte != null && !norte.isBlank() &&
                    sur != null && !sur.isBlank() &&
                    este != null && !este.isBlank() &&
                    oeste != null && !oeste.isBlank()) {

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

        log.debug("Generados {} criterios desde query params", criterios.size());
        return criterios;
    }
}

