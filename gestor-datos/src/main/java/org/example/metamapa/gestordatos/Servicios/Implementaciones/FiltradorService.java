package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IFiltradorService;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorOrigen;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public class FiltradorService implements IFiltradorService {

    private final IHechosRepository hechosRepository;

    public FiltradorService(IHechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    public List<Hecho> filtrarHechosDataBase(List<CondicionDeFiltrado> condiciones) {

        Specification<Hecho> origenSpec = condiciones.stream()
                .filter(c -> c instanceof PorOrigen)
                .map(CondicionDeFiltrado::toSpecification)
                .reduce(Specification::or)
                .orElse(null);

        Specification<Hecho> otrasSpec = condiciones.stream()
                .filter(c -> !(c instanceof PorOrigen))
                .map(CondicionDeFiltrado::toSpecification)
                .reduce(Specification::and)
                .orElse(null);

        Specification<Hecho> finalSpec;
        if (origenSpec != null && otrasSpec != null) {
            finalSpec = otrasSpec.and(origenSpec);
        } else if (otrasSpec != null) {
            finalSpec = otrasSpec;
        } else {
            finalSpec = origenSpec;
        }

        return hechosRepository.findAll(finalSpec);
    }

    public static List<Hecho> filtrarHechos(List<Hecho> unosHechos, List<CondicionDeFiltrado> filtros) {
        return unosHechos.stream().filter(unHecho -> filtrarHecho(unHecho,filtros))
                .toList();
    }

    private static boolean filtrarHecho(Hecho unHecho,List<CondicionDeFiltrado> filtros) {
        List<Boolean> CumplioCondiciones = filtros.stream()
                .map(unFiltro ->  cumpleElTipoDeFiltro(unHecho, unFiltro, filtros))
                .toList(); //Dado un hecho y las condiciones, mapea cada condición, si la cumple queda true y sino false. Ej: CumplioCondiciones = [T,T,F,T]
        boolean todosTrue = CumplioCondiciones.stream().allMatch(Boolean::booleanValue); //Checkea que la lista este llena de true
        return todosTrue;
    }

    private static boolean cumpleElTipoDeFiltro(Hecho unHecho, CondicionDeFiltrado unFiltro, List<CondicionDeFiltrado> filtros) {
        return filtros.stream()
                .filter( otroFiltro -> coincidenTipos(otroFiltro, unFiltro)) //esto nos deja los criterios que tenga el mismo tipo que un criterio
                .anyMatch(criterioFiltrado -> criterioFiltrado.cumpleUno(unHecho));
        // En la lista de criterios del mismo tipo, evalúo cada uno con unHecho, si uno solo cumple -> Devuelve true.
    }

    private static Boolean coincidenTipos(CondicionDeFiltrado unFiltro, CondicionDeFiltrado otroFiltro) {
        return unFiltro.getClass() == otroFiltro.getClass();
    }

}
