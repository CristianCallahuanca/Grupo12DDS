package AdministracionDeHechos.Consenso;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;

import java.util.List;

public class Absoluta extends AlgoritmoDeConsenso {
    @Override
    public boolean esConsensuado(Hecho h, List<Fuente> fuentes) {
        return fuentes.stream().allMatch(f -> f.getHechos().contains(h));
    }
}

