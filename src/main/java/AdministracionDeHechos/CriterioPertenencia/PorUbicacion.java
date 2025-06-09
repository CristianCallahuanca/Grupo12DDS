package AdministracionDeHechos.CriterioPertenencia;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;


public class PorUbicacion implements CriterioDePertenencia {
    private Ubicacion ubicacionBuscada;

    public PorUbicacion(Ubicacion ubicacion1) {
        ubicacionBuscada = ubicacion1;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getUbicacion() == ubicacionBuscada;
    }

    //return unHecho.getUbicacion().equals(ubicacionBuscada);
}
