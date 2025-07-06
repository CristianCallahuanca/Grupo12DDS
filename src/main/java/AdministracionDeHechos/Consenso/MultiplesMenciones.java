package AdministracionDeHechos.Consenso;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Servicios.ServicioDeAgregacion;

import java.util.List;

public class MultiplesMenciones extends AlgoritmoDeConsenso {

    private long fuentesConMismoHecho(Hecho unHecho, List<Fuente> fuentes){
        return fuentes.stream().filter(f -> f.getHechos().stream()
                        .anyMatch(h -> h.equals(unHecho)))
                        .count();
    }

    private boolean fuentesConMismoTituloDistintosAtributos(Hecho unHecho, List<Fuente> fuentes){
        return fuentes.stream().flatMap(f -> f.getHechos().stream())
                      .anyMatch(h2 -> h2.getTitulo().equals(unHecho.getTitulo()) && !h2.equals(unHecho));
    }

    @Override
    public boolean esConsensuado(Hecho h, List<Fuente> fuentes) {

        return fuentesConMismoHecho(h, fuentes) >=2 && !fuentesConMismoTituloDistintosAtributos(h, fuentes);
    }
}