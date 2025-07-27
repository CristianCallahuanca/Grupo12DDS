package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.EstadoHecho;
import Fuentes.Fuente;

public class PorEstado implements CriterioDePertenencia{
    private EstadoHecho unEstado;

    public PorEstado(EstadoHecho unEstado){
        this.unEstado = unEstado;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  unHecho.getEstadoHecho().equals(this.unEstado);
    }
}
