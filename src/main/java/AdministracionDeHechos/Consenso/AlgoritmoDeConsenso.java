package AdministracionDeHechos.Consenso;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Servicios.ServicioDeAgregacion;

import java.util.List;

public abstract class AlgoritmoDeConsenso {
    public List<Fuente> obtenerFuentesDelSistema(){
        return ServicioDeAgregacion.getInstancia().getFuentes();
    }

    public List<Hecho> verificar(List<Hecho> hechos) {
        return null;
    }
}
