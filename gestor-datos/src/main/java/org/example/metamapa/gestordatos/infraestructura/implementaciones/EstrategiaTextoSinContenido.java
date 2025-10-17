package org.example.metamapa.gestordatos.infraestructura.implementaciones;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstrategiaTextoSinContenido implements IEstrategiaDeteccion {

    private static final List<String> PALABRAS_CLAVE_SIGNIFICATIVAS = List.of(
            "hecho", "información", "eliminar", "borrar", "ofensivo",
            "falso", "error", "inadecuado", "violación", "privacidad",
            "motivo", "razón", "solicito", "consideren", "por favor"
    );

    //TODO si me sirve puedo poner estas palabras en el .yml o archivo


    @Override
    public boolean detectar(String texto) {
        if (texto == null || texto.isBlank()) return true;

        String[] palabras = texto.trim().split("\\s+");

        // muy corto o puede ser menos
        if (palabras.length < 5) return true;

        String normalizado = texto.toLowerCase();
        long coincidencias = PALABRAS_CLAVE_SIGNIFICATIVAS.stream()
                .filter(normalizado::contains)
                .count();

        // si no contiene al menos 1 palabra significativa, probablemente no es serio, quiero decir quizas no es una jutificacion que valga la pena tomar en serio
        return coincidencias == 0;
    }
}