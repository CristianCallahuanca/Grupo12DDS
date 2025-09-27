package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IFiltradorService;
import org.example.metamapa.gestordatos.Servicios.IHechoColeccionService;
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
import java.util.*;

import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {

    private final IColeccionesRepository coleccionesRepository;
    private final IHechoService hechosService;
    private final IFiltradorService filtradorService;
    private final IHechoColeccionService hechoColeccionService;

    public ColeccionesService(IColeccionesRepository coleccionesRepository, IHechoService hechosService, IFiltradorService filtradorService, IHechoColeccionService hechoColeccionService) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechosService = hechosService;
        this.filtradorService = filtradorService;
        this.hechoColeccionService = hechoColeccionService;
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
        dto.setNombre(coleccion.getTitulo()); // mapeo de titulo -> nombre en DTO
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setAlgoritmo(coleccion.getAlgoritmo().toString());
        //dto.setHechos(this.hechosService.hechoADTOOuts(coleccion.obtenerHechos()));

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

        List<PorOrigen> origenes = coleccion.getOrigenes().stream()
                .map(PorOrigen::new)
                .toList();

        List<CondicionDeFiltrado> condiciones = new ArrayList<>(coleccion.getCriterios());

        condiciones.addAll(origenes);

        coleccion.setCriterios(condiciones);

        System.out.println("la coleccion tiene: " +  coleccion.getCriterios().size() + " criterios de filtrado");

        List<Hecho> hechosFiltrados = hechosService.filtrarHechos(coleccion.getCriterios());

        System.out.println("la candidad de hechos obtenida: " + hechosFiltrados.size());

        coleccion.setHechosColeccion(hechosFiltrados.stream().map(h -> new HechoDeColeccion(h, false)).toList());

        System.out.println("la candidad de hechosColeccion obtenida: " + hechosFiltrados.size());

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


        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);

        if (coleccion == null) {
            return null;
        }

        System.out.println("el handle de la coleccion es:  " + coleccion.getHandle());

        condiciones.add(new PorColeccion(handle));

        System.out.println("se crearon " + condiciones.size() + " condiciones de filtrado");

        //List<Long> idHechos = this.hechoColeccionService.obtenerIdsHechosAsociadosColeccion(coleccion.getHandle());

        return this.hechosService.hechoADTOOuts(filtradorService.filtrarHechosDataBase(condiciones));

    }
//rivate static boolean filtrarHecho(Hecho unHecho,List<CondicionDeFiltrado> filtros)

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

    // Metodo para verificar si la nueva lista tiene menos elementos y obtener los faltantes
    private static List<Origen> obtenerOrigenesFaltantes(List<Origen> origenesViejos, List<Origen> origenesNuevos) {
        Set<Origen> conjuntoViejo = new HashSet<>(origenesViejos); Set<Origen> conjuntoNuevo = new HashSet<>(origenesNuevos);

        Set<Origen> faltantes = new HashSet<>(conjuntoViejo);
        faltantes.removeAll(conjuntoNuevo);

        return new ArrayList<>(faltantes);
    }

    // metodo para verificar si la nueva lista tiene más elementos y obtener los adicionales
    private static List<Origen> obtenerOrigenesAdicionales(List<Origen> origenesViejos, List<Origen> origenesNuevos) {
        Set<Origen> conjuntoViejo = new HashSet<>(origenesViejos); Set<Origen> conjuntoNuevo = new HashSet<>(origenesNuevos);

        Set<Origen> adicionales = new HashSet<>(conjuntoNuevo);
        adicionales.removeAll(conjuntoViejo);

        return new ArrayList<>(adicionales);
    }

    private void agregarHechosDeFuentesFaltantes (List<Origen> origenesAdicionales, Coleccion coleccion){
        List<CondicionDeFiltrado> quePertenezcaALosOrigenesNuevos = origenesAdicionales.stream()
                .map(unOrigen -> new PorOrigen(unOrigen))
                .collect(Collectors.toList());

        List<Hecho> hechos = this.hechosService.filtrarHechos(quePertenezcaALosOrigenesNuevos);
        coleccion.agregarHechos(hechos);
        coleccionesRepository.save(coleccion);
    }

    private void quitarHechosDeFuentesFaltantes (List<Origen> origenesFaltantes, Coleccion coleccion){
        List<Origen> origenesConLosQueMeQuedo = new ArrayList<>(Arrays.asList(Origen.DINAMICA, Origen.ESTATICA, Origen.PROXY));
        origenesConLosQueMeQuedo.removeAll(origenesFaltantes);

        List<CondicionDeFiltrado> quePertenezcaALosOrigenesRestantes = origenesConLosQueMeQuedo.stream()
                .map(unOrigen -> new PorOrigen(unOrigen))
                .collect(Collectors.toList());

        List<Hecho> hechos = filtradorService.filtrarHechos(coleccion.obtenerHechos(), quePertenezcaALosOrigenesRestantes);
        coleccion.reemplazarHechoDeColeccion(hechos);

        coleccionesRepository.save(coleccion);
    }

    private void actualizarHechosDeOrigenes(Coleccion coleccion, List<Origen> origenesNuevos){
        List<Origen> origenesViejos = coleccion.getOrigenes();

        List<Origen> origenesFaltantes   = obtenerOrigenesFaltantes(origenesViejos, origenesNuevos);
        List<Origen> origenesAdicionales = obtenerOrigenesAdicionales(origenesViejos, origenesNuevos);

        quitarHechosDeFuentesFaltantes (origenesFaltantes, coleccion);
        agregarHechosDeFuentesFaltantes (origenesAdicionales, coleccion);
    }

    public ColeccionOutputDTO updateFuente(List<Integer> origenes, String handle) {
        List<Origen> origenesNuevos = integerToOrigen(origenes);
        if(origenesNuevos.isEmpty()){
            return null;
        }

        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if (coleccion == null) {
            return null;
        }

        actualizarHechosDeOrigenes(coleccion, origenesNuevos);

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
