package org.example.metamapa.gestordatos.infraestructura.implementaciones;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstrategiaTextoSinContenido implements IEstrategiaDeteccion {

    private static final int MIN_WORDS = 4;         // mínimo razonable
    private static final int MIN_LETTERS = 12;      // “contenido real” en letras
    private static final double MIN_LETTER_RATIO = 0.50; // si es casi todo símbolos/números

    @Override
    public boolean detectar(String texto) {
        if (texto == null) return true;

        String t = texto.trim();
        if (t.isBlank()) return true;

        String[] palabras = t.split("\\s+");
        if (palabras.length < MIN_WORDS) return true;

        long total = t.length();
        long letras = t.chars().filter(Character::isLetter).count();

        if (letras < MIN_LETTERS) return true;

        double ratio = (double) letras / total;
        return ratio < MIN_LETTER_RATIO;
    }
}