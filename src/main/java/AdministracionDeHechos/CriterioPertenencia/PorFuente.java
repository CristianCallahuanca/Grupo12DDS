package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;

public class PorFuente implements CriterioDePertenencia {
    private Fuente unaFuente;

    public PorFuente(Fuente laFuente){
        this.unaFuente = laFuente;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  unHecho.getFuente().equals(this.unaFuente);
    }
}
