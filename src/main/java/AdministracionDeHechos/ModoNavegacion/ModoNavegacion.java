package AdministracionDeHechos.ModoNavegacion;
import AdministracionDeHechos.Consenso.AlgoritmoDeConsenso;
import AdministracionDeHechos.Hecho;

import java.util.List;

public interface ModoNavegacion {

    //El segundo parametro es un Object por temas de extensibilidad/escalabilidad
     public List<Hecho> aplicarModoDeNavegacion (List<Hecho> hechos, Object modo);
}