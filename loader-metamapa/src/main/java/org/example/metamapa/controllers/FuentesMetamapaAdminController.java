package org.example.metamapa.controllers;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.models.dtos.FuenteMetamapaDTO;
import org.example.metamapa.models.dtos.FuenteMetamapaRequest;
import org.example.metamapa.service.IFuentesMetamapaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuenteMetamapa/admin/fuentes")
@RequiredArgsConstructor
public class FuentesMetamapaAdminController {

    private final IFuentesMetamapaService fuentesMetamapaService;

    @PostMapping
    public ResponseEntity<FuenteMetamapaDTO> registrarFuenteMetamapa(
            @RequestBody FuenteMetamapaRequest request) {

        FuenteMetamapaDTO fuente = fuentesMetamapaService.registrarFuenteMetamapa(
                request.getNombreFuente(),
                request.getBaseUrl()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(fuente);
    }

    @GetMapping
    public ResponseEntity<List<FuenteMetamapaDTO>> listarFuentesMetamapa() {
        List<FuenteMetamapaDTO> fuentes = fuentesMetamapaService.listarFuentesMetamapa();
        if (fuentes.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(fuentes);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarFuente(@PathVariable Long id) {
        fuentesMetamapaService.desactivarFuente(id);
        return ResponseEntity.noContent().build(); // 204
    }

}
