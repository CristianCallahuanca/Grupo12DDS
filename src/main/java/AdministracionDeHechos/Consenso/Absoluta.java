package AdministracionDeHechos.Consenso;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;

import java.util.List;

public class Absoluta extends AlgoritmoDeConsenso {
    @Override
    public List<Hecho> verificar(List<Hecho> hechos){
        List<Fuente> todasLasFuentes = obtenerFuentesDelSistema();
        return hechos.stream()
                .filter(h->todasLasFuentes.stream().allMatch(f -> f.getHechos().contains(h))).toList();

    }
}
