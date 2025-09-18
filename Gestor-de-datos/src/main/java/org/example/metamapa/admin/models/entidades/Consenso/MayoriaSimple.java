package org.example.metamapa.admin.models.entidades.Consenso;
import org.example.metamapa.admin.models.entidades.Hecho;
import org.example.metamapa.admin.models.entidades.Origen;

public class MayoriaSimple implements AlgoritmoConsenso {
    @Override
    public boolean esConsensuado(Hecho hecho) {
        int cantidadFuentes = hecho.getOrigenes().size();
        return cantidadFuentes > (Origen.values().length / 2);
    }
}
