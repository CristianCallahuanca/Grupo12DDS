package org.example.metamapa.gestordatos.Controladores;


import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gestordatos")
public class HechosController {

    private final IHechoService hechosService;

    public HechosController(IHechoService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping("/hechos")
    public ResponseEntity<List<HechoOutputDTO>> obtenerHechos(
            @RequestParam(required = false) String tituloBuscado,
            @RequestParam(required = false) String categoriaDeseada,
            @RequestParam(required = false) String fraseClave,
            @RequestParam(required = false) String etiquetaDeseada,
            @RequestParam(required = false) String unOrigen,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud,
            @RequestParam(required = false) String desdeCarga,
            @RequestParam(required = false) String hastaCarga,
            @RequestParam(required = false) String desdeAcontecimiento,
            @RequestParam(required = false) String hastaAcontecimiento,
            @RequestParam(required = false) String estadoHecho,
            @RequestParam(required = false) Boolean sinCategorizar,
            @RequestParam(required = false) String idContribuyente,
            @RequestParam(required = false) Long idHecho,
            @RequestParam(required = false) Double norte,
            @RequestParam(required = false) Double sur,
            @RequestParam(required = false) Double este,
            @RequestParam(required = false) Double oeste)
            {

        List<CriterioRequest> criterios = construirCriteriosDesdeQueryParams(
                tituloBuscado, categoriaDeseada, fraseClave, etiquetaDeseada, unOrigen,
                latitud, longitud, desdeCarga, hastaCarga, desdeAcontecimiento, hastaAcontecimiento,
                estadoHecho, sinCategorizar, idContribuyente, idHecho, norte, sur, este, oeste
        );

        System.out.println("obtuve del query params " + criterios.size());

        return ResponseEntity.status(200).body(hechosService.buscarTodosLosHechos(criterios));
    }

    private List<CriterioRequest> construirCriteriosDesdeQueryParams(
            String tituloBuscado, String categoriaDeseada, String fraseClave,
            String etiquetaDeseada, String unOrigen, Double latitud, Double longitud,
            String desdeCarga, String hastaCarga, String desdeAcontecimiento,
            String hastaAcontecimiento, String estadoHecho, Boolean sinCategorizar,
            String idContribuyente, Long idHecho, Double norte, Double sur, Double este, Double oeste) {

        List<CriterioRequest> criterios = new ArrayList<>();

        //por area de un cuadrado
        if (norte != null && sur != null && este != null && oeste != null) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porAreaVisible");
            criterio.setParams(Map.of(
                    "norte", norte.toString(),
                    "sur", sur.toString(),
                    "este", este.toString(),
                    "oeste", oeste.toString()
            ));
            criterios.add(criterio);
        }

        // PorTitulo
        if (tituloBuscado != null && !tituloBuscado.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porTitulo");
            criterio.setParams(Map.of("tituloBuscado", tituloBuscado));
            criterios.add(criterio);
        }

        // PorCategoria
        if (categoriaDeseada != null && !categoriaDeseada.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porCategoria");
            criterio.setParams(Map.of("categoriaDeseada", categoriaDeseada));
            criterios.add(criterio);
        }

        // PorDescripcion
        if (fraseClave != null && !fraseClave.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porDescripcion");
            criterio.setParams(Map.of("fraseClave", fraseClave));
            criterios.add(criterio);
        }

        // PorEtiqueta
        if (etiquetaDeseada != null && !etiquetaDeseada.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porEtiqueta");
            criterio.setParams(Map.of("etiquetaDeseada", etiquetaDeseada));
            criterios.add(criterio);
        }

        // PorOrigen
        if (unOrigen != null && !unOrigen.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porOrigen");
            criterio.setParams(Map.of("unOrigen", unOrigen));
            criterios.add(criterio);
        }

        // PorUbicacion (requiere tanto latitud como longitud)
        if (latitud != null && longitud != null) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porUbicacion");
            criterio.setParams(Map.of(
                    "latitud", latitud.toString(),
                    "longitud", longitud.toString()
            ));
            criterios.add(criterio);
        }

        // PorFechaCargaDesde
        if (desdeCarga != null && !desdeCarga.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porFechaCargaDesde");
            criterio.setParams(Map.of("desde", desdeCarga));
            criterios.add(criterio);
        }

        // PorFechaCargaHasta
        if (hastaCarga != null && !hastaCarga.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porFechaCargaHasta");
            criterio.setParams(Map.of("hasta", hastaCarga));
            criterios.add(criterio);
        }

        // PorFechaAcontecimientoDesde
        if (desdeAcontecimiento != null && !desdeAcontecimiento.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porFechaAcontecimientoDesde");
            criterio.setParams(Map.of("desde", desdeAcontecimiento));
            criterios.add(criterio);
        }

        // PorFechaAcontecimientoHasta
        if (hastaAcontecimiento != null && !hastaAcontecimiento.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porFechaAcontecimientoHasta");
            criterio.setParams(Map.of("hasta", hastaAcontecimiento));
            criterios.add(criterio);
        }

        // PorEstado
        if (estadoHecho != null && !estadoHecho.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porEstado");
            criterio.setParams(Map.of("estadoHecho", estadoHecho));
            criterios.add(criterio);
        }

        // PorSinCategorizar
        if (sinCategorizar != null) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porSinCategorizar");
            criterio.setParams(Map.of("sinCategorizar", sinCategorizar.toString()));
            criterios.add(criterio);
        }

        // PorIdContribuyente
        if (idContribuyente != null && !idContribuyente.trim().isEmpty()) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porIdContribuyente");
            criterio.setParams(Map.of("idBuscado", idContribuyente));
            criterios.add(criterio);
        }

        // PorIDHecho
        if (idHecho != null) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porIDHecho");
            criterio.setParams(Map.of("idBuscado", idHecho.toString()));
            criterios.add(criterio);
        }

        if (norte != null && sur != null && este != null && oeste != null) {
            CriterioRequest criterio = new CriterioRequest();
            criterio.setTipo("porAreaVisible");
            criterio.setParams(Map.of(
                    "norte", norte.toString(),
                    "sur", sur.toString(),
                    "este", este.toString(),
                    "oeste", oeste.toString()
            ));
            criterios.add(criterio);
        }

        return criterios;
    }

}
