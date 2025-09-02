package org.example.metamapa.publica.clientes;

import org.example.metamapa.publica.models.dtos.input.FiltroDTO;
import org.example.metamapa.publica.models.dtos.input.HechoInputDTO;
import org.example.metamapa.publica.models.dtos.input.ModoNavegacionDTO;
import org.example.metamapa.publica.models.dtos.input.ReporteDTO;
import org.example.metamapa.publica.models.dtos.input.SolicitudDTO;
import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "admin-datos", url = "${admin.datos.url}")
public interface AdministradorClient {

    @GetMapping("/colecciones/{id}/hechos")
    List<HechoInputDTO> obtenerHechosPorColeccion(@PathVariable String id);

    @PostMapping("/navegacion/filtrada")
    List<HechoInputDTO> navegacionFiltrada(@RequestBody FiltroDTO filtro);

    @PostMapping("/navegacion/coleccion/{id}/modo")
    List<HechoInputDTO> navegacionPorModo(@PathVariable String id, @RequestBody ModoNavegacionDTO modo);

    @PostMapping("/solicitudes")
    void enviarSolicitudDeEliminacion(@RequestBody SolicitudDTO solicitudDTO);

    @PostMapping("/reportes")
    void enviarReporteDeHecho(@RequestBody ReporteDTO reporteDTO);
}


