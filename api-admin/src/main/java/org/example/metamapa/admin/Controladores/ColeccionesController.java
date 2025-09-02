package org.example.metamapa.admin.Controladores;

import org.example.metamapa.admin.Servicios.IColeccionesService;
import org.example.metamapa.admin.Servicios.Implementaciones.ColeccionesService;
import lombok.RequiredArgsConstructor;
import org.example.metamapa.admin.models.dtos.input.AlgoritmoConsensoInputDTO;
import org.example.metamapa.admin.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.admin.models.dtos.input.FuenteInputDTO;
import org.example.metamapa.admin.models.dtos.output.ColeccionOutputDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionesController {

    private final IColeccionesService coleccionesService;

    @GetMapping
    public List<ColeccionOutputDTO> obtenerTodas() {
        return coleccionesService.obtenerColecciones();
    }

    @PostMapping
    public ColeccionOutputDTO crear(@RequestBody ColeccionInputDTO dto) {
        return coleccionesService.crearColeccion(dto);
    }

    @PutMapping("/{id}")
    public ColeccionOutputDTO editar(@PathVariable Long id, @RequestBody ColeccionInputDTO dto) {
        return coleccionesService.editarColeccion(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        coleccionesService.eliminarColeccion(id);
    }

    @PutMapping("/{id}/consenso")
    public ColeccionOutputDTO cambiarAlgoritmo(@PathVariable Long id, @RequestBody AlgoritmoConsensoInputDTO dto) {
        return coleccionesService.cambiarAlgoritmo(id, dto.getAlgoritmo());
    }

    @PostMapping("/{id}/fuentes")
    public ColeccionOutputDTO agregarFuente(@PathVariable Long id, @RequestBody FuenteInputDTO dto) {
        return coleccionesService.agregarFuente(id, dto);
    }

    @DeleteMapping("/{id}/fuentes/{idFuente}")
    public ColeccionOutputDTO quitarFuente(@PathVariable Long id, @PathVariable Long idFuente) {
        return coleccionesService.quitarFuente(id, idFuente);
    }
}
