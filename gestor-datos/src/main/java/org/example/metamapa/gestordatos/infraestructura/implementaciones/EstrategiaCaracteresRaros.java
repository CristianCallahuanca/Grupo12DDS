package org.example.metamapa.gestordatos.infraestructura.implementaciones;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaCaracteresRaros implements IEstrategiaDeteccion {
    @Override
    public boolean detectar(String texto) {
        if (texto == null || texto.isBlank()) return false;

        // Si más del 30% de los caracteres no son letras, números ni espacios
        long total = texto.length();
        long raros = texto.chars()
                .filter(c -> !Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
                .count();

        double ratio = (double) raros / total;
        return ratio > 0.3;
    }
}