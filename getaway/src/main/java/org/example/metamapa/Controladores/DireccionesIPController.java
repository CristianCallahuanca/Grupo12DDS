package org.example.metamapa.Controladores;

import org.example.metamapa.Servicios.IDireccionesIpService;
import org.example.metamapa.models.entidades.DireccionesIP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getaway")  // Cambié a /api/ip para evitar conflicto con el gateway
public class DireccionesIPController {

    private final IDireccionesIpService direccionesIpService;

    DireccionesIPController(IDireccionesIpService direccionesIpService){
        this.direccionesIpService = direccionesIpService;
    }

    // ✅ Crear / Guardar IP
    @PostMapping
    public ResponseEntity<DireccionesIP> crear(@RequestBody DireccionesIP direccionIP) {
        DireccionesIP guardada = direccionesIpService.guardar(direccionIP);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }

    // ✅ Eliminar IP por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        direccionesIpService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Listar todas
    @GetMapping
    public ResponseEntity<List<DireccionesIP>> listar() {
        List<DireccionesIP> direcciones = direccionesIpService.obtenerTodas();
        return ResponseEntity.ok(direcciones);
    }

}
