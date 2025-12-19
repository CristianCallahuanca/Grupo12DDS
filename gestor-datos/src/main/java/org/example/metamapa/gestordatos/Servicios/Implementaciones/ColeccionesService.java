package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.Servicios.IColeccionesService;
import org.example.metamapa.gestordatos.Servicios.IHechoColeccionService;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.ModosNavegacion.ModoNavegacion;
import org.example.metamapa.gestordatos.models.dtos.input.ColeccionInputDTO;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.ColeccionOutputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.*;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorColeccion;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorTipoFuente;
import org.example.metamapa.gestordatos.models.entidades.enums.TipoFuente;
import org.example.metamapa.gestordatos.models.repositorios.IColeccionesRepository;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorOrigenReal;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ColeccionesService implements IColeccionesService {

    private final IColeccionesRepository coleccionesRepository;
    private final IHechoService hechoService;
    private final FiltradorService filtradorService;
    private final IHechoColeccionService hechoColeccionService; // (por si lo usás luego)
    private final IHechosRepository repoHechos;

    /*
       ===============   ADMINISTRATIVOS   ==================
     */

    @Override
    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO dto) {
        log.info("Creando colección '{}'", dto.getTitulo());

        // 1) Convertir DTO a entidad
        Coleccion coleccion = dtoToEntidad(dto);

        // 2) Inyectar criterios de tipoFuente y origenReal
        inyectarCriteriosDeOrigen(coleccion);

        // 3) Obtener hechos iniciales según criterios
        List<Hecho> hechosIniciales = obtenerHechosIniciales(coleccion);

        // 4) Asociar hechos a la colección
        asociarHechos(coleccion, hechosIniciales);

        // 5) Persistir en base de datos
        coleccionesRepository.save(coleccion);
        log.info("Colección '{}' creada con {} hechos", dto.getTitulo(), hechosIniciales.size());

        // 6) Devolver DTO de salida
        return toOutputDTO(coleccion);
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
    @Transactional
    public boolean actualizarColeccion(String handle, ColeccionInputDTO cambios) {

        return coleccionesRepository.findById(handle).map(c -> {

            // 1️⃣ Metadata
            c.setTitulo(cambios.getTitulo());
            c.setDescripcion(cambios.getDescripcion());
            c.setAlgoritmo(
                    StringAObjetos.algoritmoConsensoFactory(
                            cambios.getAlgoritmoConsenso()
                    )
            );

            // 2️⃣ Orígenes reales
            c.getOrigenesReales().clear();
            c.getOrigenesReales().addAll(cambios.getOrigenesReales());

            // 3️⃣ Criterios (REEMPLAZO TOTAL)
            c.getCriterios().clear();
            List<CondicionDeFiltrado> nuevosCriterios =
                    cambios.getCriterios().stream()
                            .map(StringAObjetos::criterioFactory)
                            .toList();
            c.getCriterios().addAll(nuevosCriterios);

            inyectarCriteriosDeOrigen(c);

            // 4️⃣ Recalcular hechos que cumplen
            List<Hecho> hechosQueCumplen = obtenerHechosIniciales(c);

            // 🔥 5️⃣ BORRAR TODAS LAS ASOCIACIONES VIEJAS
            // orphanRemoval=true → Hibernate hace DELETE
            c.getHechosColeccion().clear();

            // 🔥 6️⃣ CREAR NUEVAS ASOCIACIONES
            for (Hecho h : hechosQueCumplen) {
                HechoDeColeccion hc = new HechoDeColeccion();
                hc.setColeccion(c);
                hc.setHecho(h);
                hc.setConsensuado(false);
                c.getHechosColeccion().add(hc);
            }

            // 7️⃣ Guardar solo la colección
            coleccionesRepository.save(c);

            log.info(
                    "Colección {} actualizada. Hechos asociados: {}",
                    handle,
                    hechosQueCumplen.size()
            );

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
    public ColeccionOutputDTO actualizarOrigenesReales(String handle, List<String> nuevosOrigenes) {
        return coleccionesRepository.findById(handle).map(coleccion -> {

            List<String> viejosOrigenes = coleccion.getOrigenesReales();
            List<String> faltantes = obtenerDiferencia(viejosOrigenes, nuevosOrigenes);
            List<String> adicionales = obtenerDiferencia(nuevosOrigenes, viejosOrigenes);

            //agregar hechos de orígenes nuevos
            if (!adicionales.isEmpty()) {
                List<CondicionDeFiltrado> condiciones = adicionales.stream()
                        .map(a -> (CondicionDeFiltrado) new PorOrigenReal(a))
                        .toList();

                List<Hecho> nuevosHechos = hechoService.filtrarHechos(condiciones);
                coleccion.agregarHechos(nuevosHechos);
            }

            //quitar hechos de orígenes removidos
            if (!faltantes.isEmpty()) {
                coleccion.removerHechosPorOrigenes(faltantes);
            }

            // Actualizar lista de orígenes
            coleccion.setOrigenesReales(nuevosOrigenes);
            coleccionesRepository.save(coleccion);

            log.info("Actualizados orígenes reales en colección {}: ahora {}", handle, nuevosOrigenes);
            return toOutputDTO(coleccion);
        }).orElse(null);
    }

    private List<String> obtenerDiferencia(List<String> a, List<String> b) {
        Set<String> setA = new HashSet<>(Optional.ofNullable(a).orElse(List.of()));
        Set<String> setB = new HashSet<>(Optional.ofNullable(b).orElse(List.of()));
        return setA.stream().filter(x -> !setB.contains(x)).toList();
    }



    @Override
    public void aplicarConsensoATodas() {
        coleccionesRepository.findAll().forEach(c -> {try {
            if (c.getAlgoritmo() != null) {
                c.aplicarConsenso(repoHechos);
                coleccionesRepository.save(c);
                hechoColeccionService.actualizarHechosDeColeccion(c.getHechosColeccion());
                log.debug("Consenso aplicado en colección {}", c.getHandle());
            }
        } catch (Exception e) {
            log.error("Error aplicando consenso a colección {}: {}", c.getHandle(), e.getMessage());
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
    public List<HechoOutputDTO> retrieveHechosColeccion(String handle, Map<String, String> queryParams) {
        Optional<Coleccion> opt = coleccionesRepository.findById(handle);
        if (opt.isEmpty()) return null;

        List<CriterioRequest> criterios = StringAObjetos.convertirQueryParamsACriterios(queryParams);

        List<CondicionDeFiltrado> condiciones = criterios.stream()
                .map(StringAObjetos::criterioFactory)
                .collect(Collectors.toList());

        condiciones.add(new PorColeccion(handle));

        log.debug("Filtrando hechos de colección {} con {} condiciones", handle, condiciones.size());

        return hechoService.hechoADTOOuts(
                filtradorService.filtrarHechosDataBase(condiciones)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<HechoOutputDTO> retrieveColeccionModoNavegacion(String handle, Map<String, String> queryParams) {
        Coleccion coleccion = coleccionesRepository.findById(handle).orElse(null);
        if (coleccion == null) return null;

        String modoParam = queryParams.get("modo");
        if (modoParam == null || modoParam.isBlank()) {
            log.warn("No se proporcionó modo de navegación para la colección {}", handle);
            return null;
        }
        ModoNavegacion modo;
        try {
            modo = StringAObjetos.modoNavegacionFactory(modoParam);
        } catch (IllegalArgumentException e) {
            log.warn("Modo inválido recibido: {}", modoParam);
            return null;
        }

        List<Hecho> hechos = coleccion.obtenerHechosPorModo(modo);

        return hechoService.hechoADTOOuts(hechos);
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
        return new ColeccionOutputDTO(
                coleccion.getHandle(),
                coleccion.getTitulo(),
                coleccion.getDescripcion(),
                coleccion.getOrigenesReales(),
                coleccion.getAlgoritmo() != null ? coleccion.getAlgoritmo().getNombre() : null
        );
    }


    private Coleccion dtoToEntidad(ColeccionInputDTO dto) {
        String handle = dto.getTitulo()
                .toLowerCase()
                .replaceAll("\\s+", "-")
                + "-" + System.currentTimeMillis();

        var algoritmo = StringAObjetos.algoritmoConsensoFactory(dto.getAlgoritmoConsenso());
        var criterios = dto.getCriterios().stream()
                .map(StringAObjetos::criterioFactory)
                .toList();

        Coleccion coleccion = new Coleccion(handle, null, new ArrayList<>(),
                dto.getTitulo(), dto.getDescripcion(), criterios, algoritmo);



        coleccion.setOrigenesReales(dto.getOrigenesReales());

        return coleccion;
    }



    private void inyectarCriteriosDeOrigen(Coleccion coleccion) {
        List<CondicionDeFiltrado> criterios = new ArrayList<>(
                coleccion.getCriterios() != null ? coleccion.getCriterios() : List.of()
        );

        if (coleccion.getOrigenesReales() != null && !coleccion.getOrigenesReales().isEmpty()) {
            criterios.addAll(
                    coleccion.getOrigenesReales().stream()
                            .map(PorOrigenReal::new)
                            .toList()
            );
        }

        coleccion.setCriterios(criterios);
        log.debug("Colección {} con {} criterios (tipoFuente + origenesReales)",
                coleccion.getHandle(), criterios.size());
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


}
