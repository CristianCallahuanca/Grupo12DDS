package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;

import java.time.LocalDate;
import java.util.List;

public abstract class AlgoritmoConsenso {

    public abstract boolean esConsensuado(Hecho hecho, List<Hecho> hechosDeColeccion);
    public abstract String getNombre();
    public void consensuarHechos(List<HechoDeColeccion> hechosDeColeccion) {
        List<Hecho> hechos = hechosDeColeccion.stream()
                .map(HechoDeColeccion::getHecho)
                .toList();

        for (HechoDeColeccion hechoColeccion : hechosDeColeccion) {
            Hecho hecho = hechoColeccion.getHecho();
            boolean consensuado = esConsensuado(hecho, hechos);
            hechoColeccion.setConsensuado(consensuado);
        }
    }

    //=================LÓGICA PARA ENCONTRAR HECHOS DUPLICADOS ==============
    protected static final double RADIO_CERCANO_METROS = 30.0;
    protected static final double RADIO_RAZONABLE_METROS = 100.0;
    protected boolean tienenElMismoContenido(Hecho h1, Hecho h2) {
        //Misma fecha (día)
        LocalDate f1 = h1.getFechaAcontecimiento().toLocalDate();
        LocalDate f2 = h2.getFechaAcontecimiento().toLocalDate();
        if (!f1.equals(f2)) return false;

        //Calcular distancia
        double distancia = distanciaMetros(h1, h2);

        // Caso 1: misma fecha y distancia muy corta (<30 m)
        if (distancia < RADIO_CERCANO_METROS) return true;

        // Caso 2: misma fecha, misma categoría y distancia razonable (<100 m)
        if (distancia < RADIO_RAZONABLE_METROS) return mismaCategoria(h1, h2);

        return mismoTitulo(h1, h2);
    }

    protected boolean mismoTitulo(Hecho h1, Hecho h2) {
        if(h1.getTitulo() == null || h2.getTitulo() == null) return false;
        return h1.getTitulo().equalsIgnoreCase(h2.getTitulo());
    }

    protected boolean mismaCategoria(Hecho h1, Hecho h2){
        if(h1.getCategoria() == null || h2.getCategoria() == null) return false;
        if(h1.getCategoria().getNombre() == null || h2.getCategoria().getNombre() == null) return false;
        return h1.getCategoria().getNombre().equalsIgnoreCase(h2.getCategoria().getNombre());
    }

    //fórmula de Haversine
    protected double distanciaMetros(Hecho h1, Hecho h2) {
        double lat1 = h1.getUbicacion().getLatitud();
        double lon1 = h1.getUbicacion().getLongitud();
        double lat2 = h2.getUbicacion().getLatitud();
        double lon2 = h2.getUbicacion().getLongitud();

        double R = 6371000; // Radio de la Tierra en metros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
