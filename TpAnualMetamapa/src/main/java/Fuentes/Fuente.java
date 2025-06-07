package Fuentes;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Fuente {

    List<Hecho> hechos;

    public List<Hecho> obtenerHechos(){
        return hechos;
    }

    public List<Hecho> filtrarHechos(List<CriterioDePertenencia> criterios){
        return hechos.stream().filter(unHecho -> this.filtarHecho(unHecho, criterios))
                .toList();

    }

    private boolean filtarHecho(Hecho unHecho,List<CriterioDePertenencia> criterios) {
        List<Boolean> CumplioCondiciones = criterios.stream()
                .map(unCriterio ->  cumpleElTipoDeCriterio(unHecho, unCriterio, criterios))
                .toList();
        //Dado un hecho y las condiciones, mapea cada condición, si la cumple queda true y sino false. Ej: CumplioCondiciones = [T,T,F,T]

        boolean todosTrue = CumplioCondiciones.stream().allMatch(Boolean::booleanValue); //Checkea que la lista este llena de true
        return todosTrue;
    }

    private boolean cumpleElTipoDeCriterio(Hecho unHecho, CriterioDePertenencia unCriterio, List<CriterioDePertenencia> criterios) {
        return criterios.stream()
                .filter( otroCriterio -> this.coincidenTipos(otroCriterio, unCriterio)) //esto nos deja los criterios que tenga el mismo tipo que un criterio
                .anyMatch(criterioFiltrado -> criterioFiltrado.cumpleUno(unHecho));
        // En la lista de criterios del mismo tipo, evalúo cada uno con unHecho, si uno solo cumple -> Devuelve true.
    }

    private Boolean coincidenTipos(CriterioDePertenencia unCriterio, CriterioDePertenencia otroCriterio) {
        return unCriterio.getClass() == otroCriterio.getClass();
    }
}


//  [ubicación = BSAS, Categoría = Accidentes, Fecha = 13/09/2020,Ubicación = Mendoza] -> [T,T,F,T] ->  allTrue?



