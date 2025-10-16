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
import org.example.metamapa.gestordatos.models.entidades.*;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorColeccion;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorOrigen;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;
import org.example.metamapa.gestordatos.models.repositorios.IColeccionesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ColeccionesService implements IColeccionesService {

    private static final Logger log = LoggerFactory.getLogger(ColeccionesService.class);

    private final IColeccionesRepository coleccionesRepository;
    private final IHechoService hechoService;
    private final IFiltradorService filtradorService;
    private final IHechoColeccionService hechoColeccionService; // (por si lo usás luego)

    public ColeccionesService(IColeccionesRepository coleccionesRepository,
                              IHechoService hechoService,
                              IFiltradorService filtradorService,
                              IHechoColeccionService hechoColeccionService) {
        this.coleccionesRepository = coleccionesRepository;
        this.hechoService = hechoService;
        this.filtradorService = filtradorService;
        this.hechoColeccionService = hechoColeccionService;
    }

    /*
       ===============   ADMINISTRATIVOS   ==================
     */

    @Override
    public void crearColeccion(ColeccionInputDTO dto) {
        log.info("Creando colección '{}'", dto.getTitulo());

        Coleccion coleccion = dtoToEntidad(dto);

        // 2) Inyectar origenes (como condiciones) a los criterios de la colección
        inyectarCriteriosDeOrigen(coleccion);

        // 3) Obtener hechos iniciales según criterios
        List<Hecho> hechosIniciales = obtenerHechosIniciales(coleccion);

        // 4) Asociar hechos a la colección
        asociarHechos(coleccion, hechosIniciales);

        // 5) Persistir
        coleccionesRepository.save(coleccion);
        log.info("Colección '{}' creada con handle {}", dto.getTitulo(), coleccion.getHandle());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColeccionOutputDTO> listarColecciones() {
        return coleccionesRepository.findAll()
                .stream()
                .map(this::toOutputDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean actualizarColeccion(String handle, Map<String, String> cambios) {

        return coleccionesRepository.findById(handle).map(c -> {
            if (cambios.containsKey("titulo")) {
                c.setTitulo(cambios.get("titulo"));
            }
            if (cambios.containsKey("descripcion")) {
                c.setDescripcion(cambios.get("descripcion"));
            }
            coleccionesRepository.save(c);
            log.info("Colección {} actualizada", handle);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean eliminarColeccion(String handle) {
        return coleccionesRepository.findById(handle).map(c -> {
            coleccionesRepository.delete(c);
            log.info("Colección {} eliminada", handle);
            return true;
        }).orElse(false);
    }

    @Override
    public ColeccionOutputDTO updateAlgoritmo(String handle, String nuevoAlgoritmo) {
        return coleccionesRepository.findById(handle).map(c -> {
            c.setAlgoritmo(StringAObjetos.algoritmoConsensoFactory(nuevoAlgoritmo));
            coleccionesRepository.save(c);
            log.info("Algoritmo de consenso actualizado en colección {}", handle);
            return toOutputDTO(c);
        }).orElse(null);
    }

    @Override
    public ColeccionOutputDTO updateFuente(List<Integer> origenes, String handle) {
        if (origenes == null) return null;
        List<Origen> origenesNuevos = integersToOrigen(origenes);
        if (origenesNuevos.isEmpty()) return null;

        return coleccionesRepository.findById(handle).map(coleccion -> {
            List<Origen> origenesViejos = new ArrayList<>(coleccion.getOrigenes());

            // calcular diferencias
            List<Origen> faltantes   = obtenerOrigenesFaltantes(origenesViejos, origenesNuevos);
            List<Origen> adicionales = obtenerOrigenesAdicionales(origenesViejos, origenesNuevos);

            // aplicar cambios a nivel de hechos asociados
            if (!faltantes.isEmpty())  quitarHechosDeFuentesFaltantes(faltantes, coleccion);
            if (!adicionales.isEmpty()) agregarHechosDeFuentesAdicionales(adicionales, coleccion);

            // guardar coleccion con nuevas fuentes
            coleccion.setOrigenes(origenesNuevos);
            coleccionesRepository.save(coleccion);

            log.info("Fuentes de la colección {} actualizadas. Viejas: {}, Nuevas: {}", handle, origenesViejos, origenesNuevos);
            return toOutputDTO(coleccion);
        }).orElse(null);
    }

    @Override
    public void aplicarConsensoATodas() {
        coleccionesRepository.findAll().forEach(c -> {
            if (c.getAlgoritmo() != null) {
                c.aplicarConsenso();
                coleccionesRepository.save(c);
                log.debug("Consenso aplicado en colección {}", c.getHandle());
            }
        });
        log.info("Algoritmo de consenso aplicado a todas las colecciones");
    }

    /*
       =====================   PÚBLICA   ====================
     */

    @Override
    @Transactional(readOnly = true)
    public List<ColeccionOutputDTO> retrieveColecciones(){
        return coleccionesToDTOOuts(coleccionesRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ColeccionOutputDTO retrieveColeccion(String handle) {
        return coleccionesRepository.findById(handle)
                .map(this::toOutputDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HechoOutputDTO> retrieveHechosColeccion(String handle, List<CriterioRequest> criterios) {
        Optional<Coleccion> opt = coleccionesRepository.findById(handle);
        if (opt.isEmpty()) return null;

        // construir condiciones: criterios + pertenencia a la colección
        List<CondicionDeFiltrado> condiciones = new ArrayList<>();
        if (criterios != null) {
            condiciones.addAll(
                    criterios.stream()
                            .map(StringAObjetos::criterioFactory)
                            .collect(Collectors.toList())
            );
        }
        condiciones.add(new PorColeccion(handle));

        log.debug("Filtrando hechos de colección {} con {} condiciones", handle, condiciones.size());

        // filtrar en DB y mapear a DTO
        return hechoService.hechoADTOOuts(
                filtradorService.filtrarHechosDataBase(condiciones)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<HechoOutputDTO> retrieveColeccionModoNavegacion(String handle, String modoNavegacion, Map<String, String> queryParams) {
        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);

        if (coleccion != null) {
            List<CriterioRequest> criterios = hechoService.convertirQueryParamsACriterios(queryParams);

            List<CondicionDeFiltrado> condiciones = new ArrayList<>();
            for (CriterioRequest criterio : criterios) {
                condiciones.add(StringAObjetos.criterioFactory(criterio));
            }

            var modo = StringAObjetos.modoNavegacionFactory(modoNavegacion);
            List<Hecho> hechos = coleccion.obtenerHechosPorModo(modo);

            // Filtrar hechos que cumplan TODAS las condiciones
            List<Hecho> hechosFiltrados = hechos.stream()
                    .filter(hecho -> condiciones.stream().allMatch(condicion -> condicion.cumpleUno(hecho)))
                    .toList();

            return hechoService.hechoADTOOuts(hechosFiltrados);

        } else {
            return null;
        }
    }

    /*
       ============   HELPERS / CONVERSORES   ===============
     */

    public List<ColeccionOutputDTO> coleccionesToDTOOuts(List<Coleccion> colecciones) {
        return colecciones.stream()
                .map(this::toOutputDTO)
                .collect(Collectors.toList());
    }

    private ColeccionOutputDTO toOutputDTO(Coleccion coleccion) {
        ColeccionOutputDTO dto = new ColeccionOutputDTO();
        dto.setNombre(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setHandle(coleccion.getHandle());
        dto.setAlgoritmo(coleccion.getAlgoritmo() != null ? coleccion.getAlgoritmo().toString() : "NINGUNO");

        return dto;
    }

    private Coleccion dtoToEntidad(ColeccionInputDTO dto) {
        String handle = dto.getTitulo()
                .toLowerCase()
                .replaceAll("\\s+", "-")
                + "-" + System.currentTimeMillis();

        List<Origen> fuentes = integersToOrigen(dto.getIdsFuentes());
        var algoritmo = StringAObjetos.algoritmoConsensoFactory(dto.getAlgoritmoConsenso());
        var criterios = dto.getCriterios().stream()
                .map(StringAObjetos::criterioFactory)
                .toList();

        log.debug("dtoToEntidad(): {} criterios creados", criterios.size());
        criterios.forEach(c -> log.debug("   - Criterio tipo: {}", c.getClass().getSimpleName()));


        return new Coleccion(handle, fuentes, dto.getTitulo(), dto.getDescripcion(), criterios, algoritmo);
    }


    private void inyectarCriteriosDeOrigen(Coleccion coleccion) {
        List<CondicionDeFiltrado> criterios = new ArrayList<>(coleccion.getCriterios() != null ? coleccion.getCriterios() : List.of());
        if (coleccion.getOrigenes() != null && !coleccion.getOrigenes().isEmpty()) {
            criterios.addAll(coleccion.getOrigenes().stream().map(PorOrigen::new).toList());
        }
        coleccion.setCriterios(criterios);
        log.debug("Colección {} con {} criterios (incluye origenes)", coleccion.getHandle(), criterios.size());
    }

    private List<Hecho> obtenerHechosIniciales(Coleccion coleccion) {
        List<CondicionDeFiltrado> condiciones = coleccion.getCriterios() != null ? coleccion.getCriterios() : List.of();
        log.debug("obtenerHechosIniciales(): Se aplicarán {} condiciones", condiciones.size());
        condiciones.forEach(c -> log.debug("   * {}", c.getClass().getSimpleName()));

        long totalHechos = hechoService.contarTodos(); // creamos este método auxiliar (ver abajo)
        log.debug("Total hechos en BD antes del filtrado: {}", totalHechos);

        List<Hecho> hechos = hechoService.filtrarHechos(condiciones);
        log.debug("Hechos iniciales para colección {}: {}", coleccion.getHandle(), hechos.size());
        return hechos;
    }


    private void asociarHechos(Coleccion coleccion, List<Hecho> hechos) {
        List<HechoDeColeccion> asociaciones = hechos.stream()
                .map(h -> {
                    HechoDeColeccion hdc = new HechoDeColeccion(h, false);
                    hdc.setColeccion(coleccion);
                    return hdc;
                })
                .toList();
        coleccion.setHechosColeccion(asociaciones);
    }

    private List<Origen> integersToOrigen(List<Integer> indices) {
        if (indices == null) return List.of();
        return indices.stream().map(i -> Origen.values()[i]).collect(Collectors.toList());
    }

    private static List<Origen> obtenerOrigenesFaltantes(List<Origen> viejos, List<Origen> nuevos) {
        Set<Origen> sViejo = new HashSet<>(viejos);
        Set<Origen> sNuevo = new HashSet<>(nuevos);
        Set<Origen> faltantes = new HashSet<>(sViejo);
        faltantes.removeAll(sNuevo);
        return new ArrayList<>(faltantes);
    }

    private static List<Origen> obtenerOrigenesAdicionales(List<Origen> viejos, List<Origen> nuevos) {
        Set<Origen> sViejo = new HashSet<>(viejos);
        Set<Origen> sNuevo = new HashSet<>(nuevos);
        Set<Origen> adicionales = new HashSet<>(sNuevo);
        adicionales.removeAll(sViejo);
        return new ArrayList<>(adicionales);
    }

    private void agregarHechosDeFuentesAdicionales(List<Origen> adicionales, Coleccion coleccion) {
        if (adicionales.isEmpty()) return;

        List<CondicionDeFiltrado> condiciones = adicionales.stream()
                .map(PorOrigen::new)
                .collect(Collectors.toList());

        List<Hecho> nuevosHechos = hechoService.filtrarHechos(condiciones);
        coleccion.agregarHechos(nuevosHechos);
        log.debug("Agregados {} hechos por nuevas fuentes {} en colección {}", nuevosHechos.size(), adicionales, coleccion.getHandle());
    }

    private void quitarHechosDeFuentesFaltantes(List<Origen> faltantes, Coleccion coleccion) {
        if (faltantes.isEmpty()) return;

        // conservar solo hechos de los orígenes restantes
        List<Origen> restantes = new ArrayList<>(Arrays.asList(Origen.DINAMICA, Origen.ESTATICA, Origen.PROXY));
        restantes.removeAll(faltantes);

        List<CondicionDeFiltrado> condicionesRestantes = restantes.stream()
                .map(PorOrigen::new)
                .collect(Collectors.toList());

        List<Hecho> hechosFiltrados = filtradorService.filtrarHechos(coleccion.obtenerHechos(), condicionesRestantes);
        coleccion.reemplazarHechoDeColeccion(hechosFiltrados);

        log.debug("Removidos hechos de fuentes {}. Colección {} ahora tiene {} hechos",
                faltantes, coleccion.getHandle(), hechosFiltrados.size());
    }
}
