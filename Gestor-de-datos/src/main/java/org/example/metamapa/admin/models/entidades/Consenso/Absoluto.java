package org.example.metamapa.admin.models.entidades.Consenso;

import org.example.metamapa.admin.models.entidades.Origen;

public class Absoluto implements AlgoritmoConsenso{
    @Override
    public boolean esConsensuado(Hecho hecho) {
        int cantidadFuentes = hecho.getOrigenes().size();
        return cantidadFuentes = Origen.values().length;
    }
}
