package org.example.metamapa.gestordatos.models.entidades.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.repositorios.IRepositorioHechos;


public class MultiplesMenciones extends AlgoritmoConsenso{

    private final IRepositorioHechos repositorioHechos;

    public MultiplesMenciones(IRepositorioHechos repositorioHechos) {
        this.repositorioHechos = repositorioHechos;
    }

    @Override
    public boolean esConsensuado(Hecho hecho) {
        return false; //puse esto para que deje de romper
        //return hecho.getOrigenes().size() > 1 && !mismoTituloDistintosAtributos(hecho);
    }

    private boolean mismoTituloDistintosAtributos(Hecho hecho){
        return repositorioHechos.findAll().stream()
        .anyMatch(h2 -> h2.getTitulo().equals(hecho.getTitulo()) && !h2.equals(hecho));
        //TERMINAR ESTO
    }
}

