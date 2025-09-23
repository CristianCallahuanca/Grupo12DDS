package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.*;
import org.example.metamapa.gestordatos.models.entidades.Consenso.Absoluto;
import org.example.metamapa.gestordatos.models.entidades.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.entidades.Consenso.MayoriaSimple;
import org.example.metamapa.gestordatos.models.entidades.Consenso.MultiplesMenciones;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;
import org.example.metamapa.gestordatos.models.entidades.Ubicacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;
import org.example.metamapa.gestordatos.models.repositorios.IColeccionesRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColeccionesService implements IColeccionesService {

    private final IColeccionesRepository coleccionesRepository;
    private final IHechosRepository hechoRepository;

    public ColeccionesService(IColeccionesRepository coleccionesRepository) {
        this.coleccionesRepository = coleccionesRepository;
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


    public CondicionDeFiltrado criterioFactory(CriterioRequest request) {
        String tipo = request.getTipo().toLowerCase();
        Map<String, String> params = request.getParams();

        return switch (tipo) {
            case "portitulo" -> new PorTitulo(params.get("tituloBuscado"));
            case "porcategoria" -> new PorCategoria(params.get("categoriaDeseada"));
            case "pordescripcion" -> new PorDescripcion(params.get("fraseClave"));
            case "poretiqueta" -> new PorEtiqueta(params.get("etiquetaDeseada"));
            case "pororigen" -> new PorOrigen(Origen.valueOf(params.get("unOrigen").toUpperCase()));
            case "porubicacion" -> new PorUbicacion(
                    new Ubicacion(
                            Double.parseDouble(params.get("latitud")),
                            Double.parseDouble(params.get("longitud"))
                    )
            );
            case "porfechacarga" -> new PorFechaCarga(
                    LocalDateTime.parse(params.get("desde")),
                    LocalDateTime.parse(params.get("hasta"))
            );
            case "porfechaacontecimiento" -> new PorFechaAcontecimiento(
                    LocalDateTime.parse(params.get("desde")),
                    LocalDateTime.parse(params.get("hasta"))
            );
            case "porestado" -> new PorEstado(EstadoHecho.valueOf(params.get("estadoHecho").toUpperCase()));
            case "porsincategorizar" -> new PorSinCategorizar(Boolean.parseBoolean(params.get("sinCategorizar")));
            case "poridcontribuyente" -> new PorIdContribuyente(params.get("idBuscado"));
            case "poridhecho" -> new PorIDHecho(Long.parseLong(params.get("idBuscado")));

            default -> throw new IllegalArgumentException("Tipo de criterio no válido: " + tipo);
        };
    }

    //De DTO de entrada a entidad
    private Coleccion dtoInToColeccion(ColeccionInputDTO dto) {

        Coleccion coleccion = new Coleccion();
        String uuid = UUID.randomUUID().toString();
        List<Origen> fuentes = dto.getIdsFuentes().stream().map(i -> Origen.values()[i]).toList();

        coleccion.setHandle(uuid);
        coleccion.setOrigenes(fuentes);
        coleccion.setTitulo(dto.getTitulo());
        coleccion.setDescripcion(dto.getDescripcion());
        coleccion.setCriterios(dto.getCriterios().stream().map(this::criterioFactory).toList());
        coleccion.setAlgoritmo(algoritmoConsensoFactory(dto.getAlgoritmoConsenso()));

        return coleccion;
    }

    public void crearColeccion(ColeccionInputDTO coleccionDTO) {

        Coleccion coleccion = dtoInToColeccion(coleccionDTO);

        List<HechoDeColeccion> hechosColeccion = new ArrayList<>();


        coleccionesRepository.save(coleccion);

    }

    public ColeccionOutputDTO coleccionToDTOOut(Coleccion coleccion) {
        ColeccionOutputDTO dto = new ColeccionOutputDTO();
        //dto.setId(coleccion.getId()); // si tenés un campo id en la entidad
        dto.setNombre(coleccion.getTitulo()); // mapeo de titulo -> nombre en DTO
        dto.setDescripcion(coleccion.getDescripcion());
        //dto.setAlgoritmoConsenso(coleccion.getAlgoritmo().toString());

        // Ejemplo: convertir los hechos de coleccion a HechoOutputDTO
      /* List<Hecho> hechos = coleccion.obtenerHechos()
       List<HechoOutputDTO> hechosDTO = hechos.stream().forEach(hecho -> hechosService.hechoADTOOut(hecho)).toList(); necesitás el hechosService?
        dto.setHechos(hechosDTO);*/

        return dto;
    }

    // ✅ De lista de entidades a lista de DTOs de salida
    public List<ColeccionOutputDTO> coleccionesToDTOOuts(List<Coleccion> colecciones) {
        return colecciones.stream()
                .map(this::coleccionToDTOOut)
                .collect(Collectors.toList());
    }

    // ✅ De lista de DTOs de entrada a lista de entidades
    public List<Coleccion> dtoInsToColecciones(List<ColeccionInputDTO> dtos) {
        return dtos.stream()
                .map(this::dtoInToColeccion)
                .collect(Collectors.toList());
    }

    public AlgoritmoConsenso algoritmoConsensoFactory(String algoritmo) {
        return switch (algoritmo.toLowerCase()) {
            case "mayoriasimple" -> new MayoriaSimple();
            case "absoluto" -> new Absoluto();
            case "multiplesmenciones" -> new MultiplesMenciones();

            default -> throw new IllegalArgumentException("Algoritmo de consenso no válido: " + algoritmo);
        };
    }

    public ColeccionOutputDTO retrieveColeccion(String handle){
        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if(coleccion == null){
            return null;
        }
        return coleccionToDTOOut(coleccion);
    }

    public ColeccionOutputDTO updateColeccion(ColeccionInputDTO nuevaColeccionDTO, String handle) {
        Coleccion nuevaColeccion = dtoInToColeccion(nuevaColeccionDTO);
        nuevaColeccion.setHandle(handle);
        //En teoria actualiza la coleccion con el mismo handle
        coleccionesRepository.save(nuevaColeccion);

        return coleccionToDTOOut(nuevaColeccion);
    }

    public boolean eliminarColeccion(String handle) {
        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if(coleccion == null){
            return false;
        }
        coleccionesRepository.delete(coleccion);
        return true;
    }

    // al final dejamos los origenes en el output?

}
