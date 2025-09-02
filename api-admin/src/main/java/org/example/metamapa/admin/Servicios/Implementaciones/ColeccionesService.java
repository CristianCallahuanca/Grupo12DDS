package org.example.metamapa.admin.Servicios.Implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.admin.Servicios.IColeccionesService;
import org.example.metamapa.admin.models.dtos.*;
import org.example.metamapa.admin.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.admin.models.dtos.input.FuenteInputDTO;
import org.example.metamapa.admin.models.dtos.output.ColeccionOutputDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ColeccionesService implements IColeccionesService {

    @Override
    public List<ColeccionOutputDTO> obtenerColecciones() {
        log.info("Obteniendo todas las colecciones...");
        return new ArrayList<>(); // Implementación futura
    }

    @Override
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO dto) {
        log.info("Creando colección con nombre: {}", dto.getNombre());
        return new ColeccionOutputDTO(); // Implementación futura
    }

    @Override
    public ColeccionOutputDTO editarColeccion(Long id, ColeccionInputDTO dto) {
        log.info("Editando colección con id: {}", id);
        return new ColeccionOutputDTO(); // Implementación futura
    }

    @Override
    public void eliminarColeccion(Long id) {
        log.info("Eliminando colección con id: {}", id);
        // Implementación futura
    }

    @Override
    public ColeccionOutputDTO cambiarAlgoritmo(Long id, String nuevoAlgoritmo) {
        log.info("Cambiando algoritmo de consenso de la colección {} a {}", id, nuevoAlgoritmo);
        return new ColeccionOutputDTO(); // Implementación futura
    }

    @Override
    public ColeccionOutputDTO agregarFuente(Long idColeccion, FuenteInputDTO fuente) {
        log.info("Agregando fuente a la colección {}: {}", idColeccion, fuente.getNombreFuente());
        return new ColeccionOutputDTO(); // Implementación futura
    }

    @Override
    public ColeccionOutputDTO quitarFuente(Long idColeccion, Long idFuente) {
        log.info("Quitando fuente {} de la colección {}", idFuente, idColeccion);
        return new ColeccionOutputDTO(); // Implementación futura
    }
}
