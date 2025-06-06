package AdministracionDeHechos.CriterioPertenencia;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;


public class PorUbicacion implements CriterioDePertenencia {
    private Ubicacion ubicacionBuscada;

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getUbicacion() == ubicacionBuscada;
    }
}
