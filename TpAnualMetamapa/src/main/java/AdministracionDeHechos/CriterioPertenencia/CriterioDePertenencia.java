package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Hecho;

public interface CriterioDePertenencia {
    public boolean cumpleUno(Hecho unHecho);
}
