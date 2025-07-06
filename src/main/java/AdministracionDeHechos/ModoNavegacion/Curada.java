package AdministracionDeHechos.ModoNavegacion;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Consenso.AlgoritmoDeConsenso;

import java.util.List;
public class Curada implements ModoNavegacion {

    @Override
    public List<Hecho> aplicarModoDeNavegacion(List<Hecho> hechos, Object algoritmoConsenso) {
        if (algoritmoConsenso instanceof AlgoritmoDeConsenso) {
            return  ((AlgoritmoDeConsenso) algoritmoConsenso).hechosConsensuados;
        } else {
            throw new IllegalArgumentException("No se está navegando con un algoritmo de consenso");
        }
    }
}