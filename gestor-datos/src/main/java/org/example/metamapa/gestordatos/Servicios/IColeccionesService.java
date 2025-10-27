package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;

import java.util.List;
import java.util.Map;

public interface IColeccionesService {

    /* ==== ADMIN ==== */
    ColeccionOutputDTO  crearColeccion(ColeccionInputDTO coleccionDTO);

    List<ColeccionOutputDTO> retrieveColecciones();

    List<ColeccionOutputDTO> listarColecciones();

    boolean actualizarColeccion(String handle, Map<String, String> cambios);

    boolean eliminarColeccion(String handle);

    ColeccionOutputDTO updateAlgoritmo(String handle, String nuevoAlgoritmo);

    ColeccionOutputDTO actualizarOrigenesReales(String handle, List<String> nuevosOrigenes);

    void aplicarConsensoATodas();

    /* ==== PÚBLICA ==== */
    ColeccionOutputDTO retrieveColeccion(String handle);

    List<HechoOutputDTO> retrieveHechosColeccion(String handle, Map<String, String> queryParams);

    List<HechoOutputDTO> retrieveColeccionModoNavegacion(String handle, Map<String, String> queryParams);
}
