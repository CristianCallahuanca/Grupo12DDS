package org.example.metamapa.gestordatos.models.entidades.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;

public class MultiplesMenciones implements AlgoritmoConsenso{

    private final IRepositorioHechos repositorioHechos;

    public MultiplesMenciones(IRepositorioHechos repositorioHechos) {
        this.repositorioHechos = repositorioHechos;
    }

    @Override
    public boolean esConsensuado(Hecho hecho) {

        return hecho.getOrigenes().size() > 1 &&
    }

    private boolean mismoTituloDistintosAtributos(Hecho hecho){
        //TERMINAR ESTO
    }

 /* private long fuentesConMismoHecho(Hecho unHecho, List<Fuente> fuentes){
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
    } */
}
