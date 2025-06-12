package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Origen;
import AdministracionDeHechos.Hecho;

public class PorOrigen implements CriterioDePertenencia{
    private Origen unOrigen;

    public PorOrigen(Origen origen) {
        this.unOrigen = origen;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho){
        return unHecho.getOrigen().equals(unOrigen);
    }
}
