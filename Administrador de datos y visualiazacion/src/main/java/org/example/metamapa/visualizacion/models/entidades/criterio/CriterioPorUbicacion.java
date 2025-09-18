package org.example.metamapa.visualizacion.models.entidades.criterio;

import org.example.metamapa.visualizacion.models.entidades.Hecho;

public class CriterioPorUbicacion implements ICriterioPertenencia {

    private String latitud;
    private String longitud;

    public CriterioPorUbicacion(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public boolean cumple(Hecho hecho) {
        return hecho.getUbicacion().getLatitud().equals(latitud)
                && hecho.getUbicacion().getLongitud().equals(longitud);
    }
}
