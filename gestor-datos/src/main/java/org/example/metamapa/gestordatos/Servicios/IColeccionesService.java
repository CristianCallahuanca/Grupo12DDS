package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;

import java.util.List;
import java.util.Map;

public interface IColeccionesService {

    /* ==== ADMIN ==== */
    void crearColeccion(ColeccionInputDTO coleccionDTO);

    List<ColeccionOutputDTO> listarColecciones();

    boolean actualizarColeccion(String handle, Map<String, String> cambios);

    boolean eliminarColeccion(String handle);

    ColeccionOutputDTO updateAlgoritmo(String handle, String nuevoAlgoritmo);

    ColeccionOutputDTO updateFuente(List<Integer> origenes, String handle);

    void aplicarConsensoATodas();

    /* ==== PÚBLICA ==== */
    ColeccionOutputDTO retrieveColeccion(String handle);

    List<HechoOutputDTO> retrieveHechosColeccion(String handle, List<CriterioRequest> criterios);

    List<HechoOutputDTO> retrieveColeccionModoNavegacion(String handle, String modoNavegacion);
}
