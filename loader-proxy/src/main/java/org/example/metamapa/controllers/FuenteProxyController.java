package org.example.metamapa.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.models.dtos.FuenteConfiguradaDTO;
import org.example.metamapa.models.entidades.FuenteConfigurada;
import org.example.metamapa.service.IRegistroFuenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fuentes")
@Slf4j
public class FuenteProxyController {

    private final IRegistroFuenteService registroFuenteService;

    public FuenteProxyController(IRegistroFuenteService registroFuenteService) {
        this.registroFuenteService = registroFuenteService;
    }

    /** POST /fuentes */
    @PostMapping
    public ResponseEntity<String> registrarFuente(@RequestBody FuenteConfiguradaDTO dto) {
        log.info("Se va a registrar una fuente");
        FuenteConfigurada nueva = new FuenteConfigurada(dto.getNombre(), dto.getUrl(), dto.getTipo());
        try {
            registroFuenteService.registrarFuente(nueva);
            return ResponseEntity.ok("Fuente registrada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** GET /fuentes */
    @GetMapping
    public ResponseEntity<List<FuenteConfiguradaDTO>> obtenerFuentes() {
        List<FuenteConfiguradaDTO> fuentes = registroFuenteService.obtenerFuentes().stream()
                .map(f -> {
                    FuenteConfiguradaDTO dto = new FuenteConfiguradaDTO();
                    dto.setNombre(f.getNombre());
                    dto.setUrl(f.getUrl());
                    dto.setTipo(f.getTipo());
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(fuentes);
    }

    /** GET /fuentes/{nombre} */
    @GetMapping("/{nombre}")
    public ResponseEntity<FuenteConfiguradaDTO> buscarPorNombre(@PathVariable String nombre) {
        Optional<FuenteConfigurada> fuenteOpt = registroFuenteService.buscarPorNombre(nombre);
        return fuenteOpt.map(f -> {
            FuenteConfiguradaDTO dto = new FuenteConfiguradaDTO();
            dto.setNombre(f.getNombre());
            dto.setUrl(f.getUrl());
            dto.setTipo(f.getTipo());
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** DELETE /fuentes/{nombre} */
    @DeleteMapping("/{nombre}")
    public ResponseEntity<String> eliminarFuente(@PathVariable String nombre) {
        try {
            registroFuenteService.eliminarFuente(nombre);
            return ResponseEntity.ok("Fuente eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
