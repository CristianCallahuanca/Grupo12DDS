package AdministracionDeHechos.ModoNavegacion;

import AdministracionDeHechos.Consenso.AlgoritmoDeConsenso;
import AdministracionDeHechos.Hecho;

import java.util.List;
public class Irrestricta implements ModoNavegacion{
    @Override
    public List<Hecho> aplicarModoDeNavegacion (List<Hecho> hechos, Object algo){
           return hechos;
    }

}
