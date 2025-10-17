package org.example.metamapa.gestordatos.infraestructura.implementaciones;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstrategiaPorPalabrasClave implements IEstrategiaDeteccion {

    private static final List<String> PALABRAS_SPAM = List.of(
            "dinero", "oferta", "click aquí", "compra", "gana rápido", "hazte rico", "$$$", "crédito fácil", "gratis", "link", "suscríbete"
    );

    //TODO si me sirve puedo poner estas palabras en el .yml o archivo

    @Override
    public boolean detectar(String texto) {
        if (texto == null || texto.isBlank()) return false;

        String normalizado = texto.toLowerCase();
        return PALABRAS_SPAM.stream().anyMatch(normalizado::contains);
    }
}