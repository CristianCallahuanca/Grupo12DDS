package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.*;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.TipoFuente;
import org.example.metamapa.gestordatos.models.repositorios.ICategoriaRepository;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HechoService implements IHechoService {

    private final IHechosRepository repositorioHechos;
    private final FiltradorService filtradorService;
    private final ICategoriaRepository categoriaRepo;


    /*
       ================ BÚSQUEDA Y FILTRADO ======================
    */

    @Override
    public HechoOutputDTO aprobarSolicitud(Long id){
        Hecho hecho =  repositorioHechos.findById(id).orElse(null);

        if(hecho == null) return null;

        hecho.setEstadoHecho(EstadoHecho.VISIBLE);
        repositorioHechos.save(hecho);

        return hechoADTOOut(hecho);
    }

    @Override
    public HechoOutputDTO aprobarSugerenciaSolicitud(Long id, String sugerencia){
        Hecho hecho =  repositorioHechos.findById(id).orElse(null);

        if(hecho == null) return null;

        hecho.setEstadoHecho(EstadoHecho.VISIBLE);
        hecho.setSugerenciaCambio(sugerencia);
        repositorioHechos.save(hecho);

        return hechoADTOOut(hecho);
    }

    @Override
    public HechoOutputDTO denegarSolicitud(Long id){
        Hecho hecho =  repositorioHechos.findById(id).orElse(null);

        if(hecho == null) return null;

        hecho.setEstadoHecho(EstadoHecho.NO_VISIBLE);
        repositorioHechos.save(hecho);

        return hechoADTOOut(hecho);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HechoOutputDTO> buscarTodosLosHechos(List<CriterioRequest> criterios) {
        List<CondicionDeFiltrado> condiciones = new ArrayList<>();

        if (criterios != null && !criterios.isEmpty()) {
            condiciones.addAll(
                    criterios.stream()
                            .map(StringAObjetos::criterioFactory)
                            .toList()
            );
        }

        log.debug("Se crearon {} condiciones de filtrado", condiciones.size());
        List<Hecho> hechos = this.filtrarHechos(condiciones);
        log.debug("Se obtuvieron {} hechos desde la base de datos", hechos.size());

        hechos = hechos.stream().filter(hecho -> hecho.getEstadoHecho() != EstadoHecho.NO_VISIBLE).collect(Collectors.toList());

        return hechoADTOOuts(hechos);
    }

    @Override
    public List<Hecho> filtrarHechos(List<CondicionDeFiltrado> condiciones) {
        if (condiciones == null || condiciones.isEmpty())
            return repositorioHechos.findAll();

        return filtradorService.filtrarHechosDataBase(condiciones);
    }

    public long contarTodos() {
        return repositorioHechos.count();
    }
    /*
       ================== EDICIÓN CONTROLADA =====================
    */

    @Override
    @Transactional
    public boolean editarHechoContribuyente(Long id, Map<String, Object> cambios) {
        Optional<Hecho> opt = repositorioHechos.findById(id);
        if (opt.isEmpty()) return false;

        Hecho hecho = opt.get();

        // En la versión final, el autor debería obtenerse del contexto o token
        ContribuyenteRegistrado autor = hecho.getContribuyente();
        if (autor == null) {
            log.warn("Hecho {} no tiene contribuyente asociado", id);
            return false;
        }

        // Mapear los cambios del request a una instancia temporal de Hecho
        Hecho cambiosHecho = new Hecho();
        cambios.forEach((campo, valor) -> {

            System.out.println(valor);

            switch (campo) {
                case "titulo" -> cambiosHecho.setTitulo((String) valor);
                case "descripcion" -> cambiosHecho.setDescripcion((String) valor);
                case "categoria" -> {
                    if (valor instanceof String nombreCategoria) {
                        Categoria categoria = categoriaRepo.findByNombreIgnoreCase(nombreCategoria)
                                .orElse(null);

                        // Si eligió "Otro" o no existe la categoría, no la seteamos (queda null)
                        if (categoria == null || nombreCategoria.equalsIgnoreCase("Otro")) {
                            cambiosHecho.setCategoria(null);
                        } else {
                            cambiosHecho.setCategoria(categoria);
                        }
                    }
                }
                case "etiqueta" -> cambiosHecho.setEtiqueta((String) valor);
                case "ubicacion" -> {
                    if (valor instanceof Map<?, ?> ubicacionMap) {
                        // Extraer latitud y longitud del mapa
                        Object latitudObj = ubicacionMap.get("latitud");
                        Object longitudObj = ubicacionMap.get("longitud");

                        // Convertir a Double y crear el objeto Ubicacion
                        if (latitudObj instanceof Number && longitudObj instanceof Number) {
                            Double latitud = ((Number) latitudObj).doubleValue();
                            Double longitud = ((Number) longitudObj).doubleValue();

                            // Crear el objeto Ubicacion (depende de cómo sea tu constructor)
                            Ubicacion ubicacion = new Ubicacion(latitud, longitud);
                            cambiosHecho.setUbicacion(ubicacion);
                        }
                    }
                }
                case "fechaAcontecimiento" -> {
                    if (valor instanceof String fechaStr)
                        cambiosHecho.setFechaAcontecimiento(LocalDateTime.parse(fechaStr));
                }
            }
        });

        try {
            hecho.editarCon(cambiosHecho, autor);
            repositorioHechos.save(hecho);
            return true;
        } catch (IllegalStateException e) {
            log.warn("Edición no autorizada para hecho {}: {}", id, e.getMessage());
            return false;
        }
    }


    private boolean puedeEditar(Hecho hecho) {
        boolean esDinamica = hecho.getTipoFuente() == TipoFuente.DINAMICA;
        boolean dentroDePlazo = hecho.getFechaCarga() != null &&
                ChronoUnit.DAYS.between(hecho.getFechaCarga(), LocalDateTime.now()) <= 7;
        return esDinamica && dentroDePlazo;
    }

    /*
       ===================== CONVERSORES =========================
     */

    @Override
    public List<HechoOutputDTO> hechoADTOOuts(List<Hecho> hechos) {
        return hechos.stream().map(this::hechoADTOOut).collect(Collectors.toList());
    }

    private HechoOutputDTO hechoADTOOut(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(String.valueOf(hecho.getHecho_id()));
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(
                hecho.getCategoria() != null ? hecho.getCategoria().getNombre() : null);
        if (hecho.getUbicacion() != null) {
            dto.setLatitud(String.valueOf(hecho.getUbicacion().getLatitud()));
            dto.setLongitud(String.valueOf(hecho.getUbicacion().getLongitud()));
        }

        dto.setFechaAcontecimiento(
                hecho.getFechaAcontecimiento() != null
                        ? hecho.getFechaAcontecimiento().toString()
                        : null
        ); //TODO: CURAR ESTO
        dto.setEtiqueta(hecho.getEtiqueta());
        dto.setArchivosMultimedia(hecho.getArchivosMultimedia());
        dto.setSugerencia_cambio(hecho.getSugerenciaCambio());

        if (hecho.getContribuyente() != null) {
            ContribuyenteRegistrado c = hecho.getContribuyente();
            dto.setNombre_contribuyente(c.getNombre());
            dto.setApellido_contribuyente(c.getApellido());
        }

        return dto;
    }

    /*
       =================== QUERY → CRITERIOS =====================
    */





}
