package org.example.metamapa.gestordatos.infraestructura.implementaciones;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EstrategiaTextoRepetitivo implements IEstrategiaDeteccion {

    // Umbrales “suaves”
    private static final double MAX_ONE_CHAR_RATIO = 0.80; // "aaaaaa..." o "11111..."
    private static final double MAX_TOP_WORD_RATIO = 0.70; // "hola hola hola..."

    @Override
    public boolean detectar(String texto) {
        if (texto == null) return false;

        String t = texto.trim();

        String lower = t.toLowerCase();

        // 1) Muchísima repetición del mismo caracter (ej: aaaaa, !!!!!, 11111)
        Map<Integer, Long> freqChars = lower.chars()
                .filter(c -> !Character.isWhitespace(c))
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long totalChars = freqChars.values().stream().mapToLong(Long::longValue).sum();
        if (totalChars > 0) {
            long maxChar = freqChars.values().stream().mapToLong(Long::longValue).max().orElse(0);
            double ratio = (double) maxChar / totalChars;
            if (ratio >= MAX_ONE_CHAR_RATIO) return true;
        }

        // 2) La misma palabra repetida domina el texto (ej: "hola hola hola hola")
        String[] words = lower.split("\\s+");
        if (words.length >= 4) {
            Map<String, Long> freqWords = Arrays.stream(words)
                    .filter(w -> w.length() > 0)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            long totalWords = freqWords.values().stream().mapToLong(Long::longValue).sum();
            long maxWord = freqWords.values().stream().mapToLong(Long::longValue).max().orElse(0);
            double ratio = (double) maxWord / totalWords;

            if (ratio >= MAX_TOP_WORD_RATIO) return true;
        }

        return false;
    }
}
