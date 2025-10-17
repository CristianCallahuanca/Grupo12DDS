package org.example.metamapa.gestordatos.infraestructura.implementaciones;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaTextoRepetitivo implements IEstrategiaDeteccion {
    @Override
    public boolean detectar(String texto) {
        if (texto == null || texto.isBlank()) return false;
        String normalizado = texto.toLowerCase();

        // Si tiene una sola palabra repetida más del 70% del texto
        long count = normalizado.chars().distinct().count();
        double repetitividad = (double) count / normalizado.length();
        return repetitividad < 0.2; // texto demasiado homogéneo o repetitivo
    }
}
