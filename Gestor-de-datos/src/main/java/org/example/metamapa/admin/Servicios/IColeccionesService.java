package org.example.metamapa.admin.Servicios;

import org.example.metamapa.admin.models.dtos.*;
import org.example.metamapa.admin.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.admin.models.dtos.input.FuenteInputDTO;
import org.example.metamapa.admin.models.dtos.output.ColeccionOutputDTO;

import java.util.List;

public interface IColeccionesService {

    List<ColeccionOutputDTO> obtenerColecciones();

    ColeccionOutputDTO crearColeccion(ColeccionInputDTO dto);

    ColeccionOutputDTO editarColeccion(Long id, ColeccionInputDTO dto);

    void eliminarColeccion(Long id);

    ColeccionOutputDTO cambiarAlgoritmo(Long id, String nuevoAlgoritmo);

    ColeccionOutputDTO agregarFuente(Long idColeccion, FuenteInputDTO fuente);

    ColeccionOutputDTO quitarFuente(Long idColeccion, Long idFuente);
}
