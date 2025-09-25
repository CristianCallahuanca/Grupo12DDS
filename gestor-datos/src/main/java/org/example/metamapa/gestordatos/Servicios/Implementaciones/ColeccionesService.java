package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IFiltradorService;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.*;
import org.example.metamapa.gestordatos.models.entidades.Consenso.Absoluto;
import org.example.metamapa.gestordatos.models.entidades.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.entidades.Consenso.MayoriaSimple;
import org.example.metamapa.gestordatos.models.entidades.Consenso.MultiplesMenciones;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;
import org.example.metamapa.gestordatos.models.entidades.ModosNavegacion.Curada;
import org.example.metamapa.gestordatos.models.entidades.ModosNavegacion.Irrestricta;
import org.example.metamapa.gestordatos.models.entidades.ModosNavegacion.ModoNavegacion;
import org.example.metamapa.gestordatos.models.entidades.Ubicacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;
import org.example.metamapa.gestordatos.models.repositorios.IColeccionesRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {

    private final IColeccionesRepository coleccionesRepository;
    private final IHechoService hechosService;
    private final IFiltradorService filtradorService;

    public ColeccionesService(IColeccionesRepository coleccionesRepository, IHechoService hechosService, IFiltradorService filtradorService) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.filtradorService = filtradorService;
    }


    // De lista de entidades a lista de DTOs de salida
    public List<ColeccionOutputDTO> coleccionesToDTOOuts(List<Coleccion> colecciones) {
        return colecciones.stream()
                .map(this::coleccionToDTOOut)
                .collect(Collectors.toList());
    }

    // De lista de DTOs de entrada a lista de entidades
    public List<Coleccion> dtoInsToColecciones(List<ColeccionInputDTO> dtos) {
        return dtos.stream()
                .map(this::dtoInToColeccion)
                .collect(Collectors.toList());
    }

    public void aplicarConsensoATodas() {
        List<Coleccion> colecciones = coleccionesRepository.findAll();
        for (Coleccion coleccion : colecciones) {
            if (coleccion.getAlgoritmo() != null) { //no sé si esta parte es necesaria
                coleccion.aplicarConsenso();
                coleccionesRepository.save(coleccion);
            }
        }
    }

    private List<Origen> integerToOrigen(List<Integer> origenes){
        return origenes.stream().map(i -> Origen.values()[i]).toList();
    }

    public ColeccionOutputDTO coleccionToDTOOut(Coleccion coleccion) {
        ColeccionOutputDTO dto = new ColeccionOutputDTO();
        //dto.setId(coleccion.getId()); // si tenés un campo id en la entidad
        dto.setNombre(coleccion.getTitulo()); // mapeo de titulo -> nombre en DTO
        dto.setDescripcion(coleccion.getDescripcion());
        //dto.setAlgoritmoConsenso(coleccion.getAlgoritmo().toString());


        return dto;
    }

    //De DTO de entrada a entidad
    private Coleccion dtoInToColeccion(ColeccionInputDTO dto) {

        String uuid = UUID.randomUUID().toString();
        List<Origen> fuentes = integerToOrigen(dto.getIdsFuentes());

        return new Coleccion(
                uuid, fuentes, dto.getTitulo(), dto.getDescripcion(),
                dto.getCriterios().stream().map(c -> StringAObjetos.criterioFactory(c)).toList(),
                StringAObjetos.algoritmoConsensoFactory(dto.getAlgoritmoConsenso())
        );
    }

    /*
    ##########
    ##Create##
    ##########
    */
    public void crearColeccion(ColeccionInputDTO coleccionDTO) {

        Coleccion coleccion = dtoInToColeccion(coleccionDTO);

        List<Hecho> hechosFiltrados = hechosService.filtrarHechos(coleccion.getCriterios());

        coleccion.setHechosColeccion(hechosFiltrados.stream().map(h -> new HechoDeColeccion(h, false)).toList());

        coleccionesRepository.save(coleccion);
    }
    /*
    ##########
    ##Create##
    ##########
    */

    /*
    ############
    ##Retrieve##
    ############
    */

    public ColeccionOutputDTO retrieveColeccion(String handle) {
        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if (coleccion == null) {
            return null;
        }
        return coleccionToDTOOut(coleccion);
    }

    public List<HechoOutputDTO> retrieveHechosColeccion(String handle, List<CriterioRequest> criterios) {

        List<CondicionDeFiltrado> condiciones = new ArrayList<>(criterios.stream().map(c -> StringAObjetos.criterioFactory(c)).toList());

        condiciones.add( new PorColeccion(handle));

        return this.hechosService.hechoADTOOuts(filtradorService.filtrarHechosDataBase(condiciones));

    }


    public List<HechoOutputDTO> retrieveColeccionModoNavegacion(String handle, String modoNavegacion){
        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if (coleccion == null) {
            return null;
        }
        return this.hechosService.hechoADTOOuts(coleccion.obtenerHechosPorModo(StringAObjetos.modoNavegacionFactory(modoNavegacion)));
    }



    /*
    ############
    ##Retrieve##
    ############
    */

    /*
    ##########
    ##Update##
    ##########
    */

    public ColeccionOutputDTO updateAlgoritmo(String handle, String nuevoAlgoritmo) {
        AlgoritmoConsenso algoritmo = StringAObjetos.algoritmoConsensoFactory(nuevoAlgoritmo);
        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if (coleccion == null) {
           return null;
        }
        coleccion.setAlgoritmo(algoritmo);
        this.coleccionesRepository.save(coleccion);
        return coleccionToDTOOut(coleccion);
    }

    public ColeccionOutputDTO updateFuente(List<Integer> origenes, String handle) {
        List<Origen> nuevoOrigen = integerToOrigen(origenes);
        if(nuevoOrigen.isEmpty()){
            return null;
        }

        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if (coleccion == null) {
            return null;
        }

        //TODO Si los origenes son menos de los que tenias hay que eliminar los hechos de la coleccion
        //TODO Si los origenes son mas de los que tenias hay que traer nuevos hechos?

        coleccion.setOrigenes(nuevoOrigen);

        coleccionesRepository.save(coleccion);

        return coleccionToDTOOut(coleccion);
    }
    /*
    ##########
    ##Update##
    ##########
    */

    /*
    ##########
    ##Delete##
    ##########
    */

    public boolean eliminarColeccion(String handle) {
        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if (coleccion == null) {
            return false;
        }
        coleccionesRepository.delete(coleccion);
        return true;
    }

    /*
    ##########
    ##Delete##
    ##########
    */

}
