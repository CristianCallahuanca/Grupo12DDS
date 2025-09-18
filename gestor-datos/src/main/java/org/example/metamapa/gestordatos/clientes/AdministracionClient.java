package org.example.metamapa.gestordatos.clientes;

import org.springframework.cloud.openfeign.FeignClient;
/*
@FeignClient(name = "modulo-administracion", url = "${modulo.administracion.url}")
public interface AdministracionClient {

    @GetMapping("/colecciones")
    List<ColeccionOutputDTO> obtenerColecciones();

    @PostMapping("/colecciones")
    ColeccionOutputDTO crearColeccion(@RequestBody ColeccionInputDTO dto);

    @PutMapping("/colecciones/{id}")
    ColeccionOutputDTO editarColeccion(@PathVariable Long id, @RequestBody ColeccionInputDTO dto);

    @DeleteMapping("/colecciones/{id}")
    void eliminarColeccion(@PathVariable Long id);

    @PutMapping("/colecciones/{id}/consenso")
    ColeccionOutputDTO cambiarAlgoritmo(@PathVariable Long id, @RequestBody AlgoritmoConsensoInputDTO dto);

    @PostMapping("/colecciones/{id}/fuentes")
    ColeccionOutputDTO agregarFuente(@PathVariable Long id, @RequestBody FuenteInputDTO dto);

    @DeleteMapping("/colecciones/{id}/fuentes/{idFuente}")
    ColeccionOutputDTO quitarFuente(@PathVariable Long id, @PathVariable Long idFuente);

    @PostMapping("/solicitudes/{id}/aprobar")
    SolicitudOutputDTO aprobarSolicitud(@PathVariable Long id);

    @PostMapping("/solicitudes/{id}/denegar")
    SolicitudOutputDTO denegarSolicitud(@PathVariable Long id);
}
*/