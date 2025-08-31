package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.filtros.FilterCondition;

import java.util.List;

public class NavegacionDeHechos {

    private static boolean coincidenTipos(FilterCondition unFiltro, FilterCondition otroFiltro) {
        return unFiltro.getClass() == otroFiltro.getClass();
    }

    private static boolean cumpleElTipoDeFiltro(Hecho unHecho, FilterCondition unFiltro, List<FilterCondition> filtros) {
        return filtros.stream()
                .filter( otroFiltro -> coincidenTipos(otroFiltro, unFiltro)) //esto nos deja los criterios que tenga el mismo tipo que un criterio
                .anyMatch(criterioFiltrado -> criterioFiltrado.cumpleUno(unHecho));
        // En la lista de criterios del mismo tipo, evalúo cada uno con unHecho, si uno solo cumple -> Devuelve true.
    }

    private static boolean filtrarHecho(Hecho unHecho,List<FilterCondition> filtros) {
        List<Boolean> CumplioCondiciones = filtros.stream()
                .map(unFiltro ->  cumpleElTipoDeFiltro(unHecho, unFiltro, filtros))
                .toList(); //Dado un hecho y las condiciones, mapea cada condición, si la cumple queda true y sino false. Ej: CumplioCondiciones = [T,T,F,T]
        boolean todosTrue = CumplioCondiciones.stream().allMatch(Boolean::booleanValue); //Checkea que la lista este llena de true
        return todosTrue;
    }

    public static List<Hecho> filtrarHechos(List<Hecho> unosHechos, List<FilterCondition> filtros) {
        return unosHechos.stream().filter(unHecho -> filtrarHecho(unHecho,filtros))
                .toList();
    }
}
