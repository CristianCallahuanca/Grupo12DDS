package org.example.metamapa.publica.clientes;

import org.example.metamapa.publica.models.dtos.input.FiltroDTO;
import org.example.metamapa.publica.models.dtos.input.HechoInputDTO;
import org.example.metamapa.publica.models.dtos.input.ModoNavegacionDTO;
import org.example.metamapa.publica.models.dtos.input.ReporteDTO;
import org.example.metamapa.publica.models.dtos.input.SolicitudDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "agregador", url = "${agregador.url}")
public interface AgregadorClient {

    @GetMapping("/colecciones/{id}/hechos")
    List<HechoInputDTO> obtenerHechosPorColeccion(@PathVariable("id") String idColeccion);

    @PostMapping("/navegacion/filtrada")
    List<HechoInputDTO> navegacionFiltrada(@RequestBody FiltroDTO filtro);

    @PostMapping("/navegacion/modo/{id}")
    List<HechoInputDTO> navegacionPorModo(@PathVariable("id") String idColeccion, @RequestBody ModoNavegacionDTO modo);

    @PostMapping("/solicitudes")
    void enviarSolicitudDeEliminacion(@RequestBody SolicitudDTO solicitud);

    @PostMapping("/reportes")
    void enviarReporteDeHecho(@RequestBody ReporteDTO reporte);
}
