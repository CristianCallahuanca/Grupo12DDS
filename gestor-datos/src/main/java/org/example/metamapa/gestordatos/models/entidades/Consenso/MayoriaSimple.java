package org.example.metamapa.gestordatos.models.entidades.Consenso;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;

public class MayoriaSimple extends AlgoritmoConsenso {
    @Override
    public boolean esConsensuado(Hecho hecho) {
        int cantidadFuentes = hecho.getOrigenes().size();
        return cantidadFuentes > (Origen.values().length / 2);
    }
}
