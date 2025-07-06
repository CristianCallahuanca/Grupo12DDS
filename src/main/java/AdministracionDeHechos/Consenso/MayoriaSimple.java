package AdministracionDeHechos.Consenso;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Servicios.ServicioDeAgregacion;

import java.util.List;

public class MayoriaSimple extends AlgoritmoDeConsenso{

    @Override
    public boolean esConsensuado(Hecho h, List<Fuente> fuentes) {
        int mitadDeFuentes = fuentes.size()/2;

        return (fuentes.stream().filter(f -> f.getHechos().contains(h))
                .count() >= mitadDeFuentes);
    }
}
