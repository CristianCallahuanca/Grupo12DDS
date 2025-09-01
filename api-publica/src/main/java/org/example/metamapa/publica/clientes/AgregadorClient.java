package org.example.metamapa.publica.clientes;

import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "agregador", url = "${agregador.url}")
public interface AgregadorClient {

    @GetMapping("/colecciones/{id}/hechos")
    List<HechoOutputDTO> obtenerHechosDeColeccion(
            @PathVariable("id") String idColeccion,
            @RequestParam Map<String, String> filtros);

    @PostMapping("/solicitudes")
    void crearSolicitudDeEliminacion(@RequestBody String idHecho);

    @PostMapping("/reportes")
    void crearReporte(@RequestBody Map<String, Object> datosReporte);
}
