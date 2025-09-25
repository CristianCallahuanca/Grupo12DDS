package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.input.FuenteInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;

import java.time.LocalDate;
import java.util.List;

public interface IColeccionesService {

    public void aplicarConsensoATodas();

    //List<ColeccionOutputDTO> obtenerColecciones();

    void crearColeccion(ColeccionInputDTO coleccionDTO);

    ColeccionOutputDTO retrieveColeccion(String handle);

    boolean eliminarColeccion(String handle);

    List<HechoOutputDTO> retrieveHechosColeccion(String handle, List<CriterioRequest> criterios);


    ColeccionOutputDTO updateAlgoritmo(String handle, String nuevoAlgoritmo);

    ColeccionOutputDTO updateFuente(List<Integer> origenes, String handle);

    List<HechoOutputDTO> retrieveColeccionModoNavegacion(String handle, String modoNavegacion);
}
