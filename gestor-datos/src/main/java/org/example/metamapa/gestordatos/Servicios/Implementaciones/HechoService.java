package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IFiltradorService;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.input.HechoInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {

    private final IHechosRepository repositorioHechos;
    private final IFiltradorService filtradorService;

    public HechoService(IHechosRepository repositorioHechos, IFiltradorService filtradorService){
        this.repositorioHechos = repositorioHechos;
        this.filtradorService = filtradorService;
    }

    public List<HechoOutputDTO> buscarTodosLosHechos(List<CriterioRequest> criterios){

        List<CondicionDeFiltrado> condiciones = new ArrayList<>(criterios.stream().map(c -> StringAObjetos.criterioFactory(c)).toList());

        return hechoADTOOuts(this.filtrarHechos(condiciones));
    }

    public void guardarHecho(HechoInputDTO hechoInputDTO){};

    public List<HechoOutputDTO> hechoADTOOuts(List<Hecho> hechos){
        return hechos.stream().map(this::hechoADTOOut).collect(Collectors.toList());
    }

    public HechoOutputDTO hechoADTOOut(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setLatitud(String.valueOf(hecho.getUbicacion().getLatitud()));
        dto.setLongitud(String.valueOf(hecho.getUbicacion().getLongitud()));
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento().toString());
        dto.setEtiqueta(hecho.getEtiqueta());
        dto.setNombre_contribuyente(hecho.getContribuyente().getNombre());
        dto.setApellido_contribuyente(hecho.getContribuyente().getApellido());
        dto.setArchivosMultimedia(hecho.getArchivosMultimedia());
        return dto;
    }

    //obtiene los hechos de la DB en base a las condiciones de filtrado

    public List<Hecho> filtrarHechos(List<CondicionDeFiltrado> condiciones) {
        return filtradorService.filtrarHechosDataBase(condiciones);
    }

    /*
    public List<Hecho> filtrarHechos(List<CondicionDeFiltrado> condiciones) {
    // Agrupar por tipo de criterio
    Map<TipoCriterio, List<CondicionDeFiltrado>> agrupados =
            condiciones.stream().collect(Collectors.groupingBy(CondicionDeFiltrado::getTipo));

    Specification<Hecho> spec = Specification.where(null);

    for (Map.Entry<TipoCriterio, List<CondicionDeFiltrado>> entry : agrupados.entrySet()) {
        // Para cada tipo: OR
        Specification<Hecho> orSpec = entry.getValue().stream()
                .map(CondicionDeFiltrado::toSpecification)
                .reduce(Specification::or)
                .orElse(null);

        // Entre tipos: AND
        if (orSpec != null) {
            spec = spec.and(orSpec);
        }
    }

    return repositorioHechos.findAll(spec);
}

     */
}
