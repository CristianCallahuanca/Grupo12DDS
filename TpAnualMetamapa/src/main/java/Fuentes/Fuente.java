package Fuentes;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import java.util.List;

public abstract class Fuente {

    private List<Hecho> hechos;

    public List<Hecho> obtenerHechos(){
        return hechos;
    }

    /*public List<Hecho> filtrarHecho(List<CriterioDePertenencia> criterios){


    }*/

}
