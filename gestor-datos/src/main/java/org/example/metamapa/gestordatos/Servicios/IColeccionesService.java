package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.FuenteInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;

import java.util.List;

public interface IColeccionesService {

    public void aplicarConsensoATodas();

    //List<ColeccionOutputDTO> obtenerColecciones();

    void crearColeccion(ColeccionInputDTO coleccionDTO);

    //ColeccionOutputDTO editarColeccion(Long id, ColeccionInputDTO dto);

    //void eliminarColeccion(Long id);

    //ColeccionOutputDTO cambiarAlgoritmo(Long id, String nuevoAlgoritmo);

    //ColeccionOutputDTO agregarFuente(Long idColeccion, FuenteInputDTO fuente);

    //ColeccionOutputDTO quitarFuente(Long idColeccion, Long idFuente);
}
