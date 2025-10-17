package org.example.metamapa.gestordatos.infraestructura.implementaciones;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaPorPalabrasClave implements IEstrategiaDeteccion {

    @Override
    public boolean detectar(String texto) {
        if (texto == null) return false;
        String lower = texto.toLowerCase();
        return lower.contains("dinero") ||
                lower.contains("$$$") ||
                lower.contains("oferta exclusiva");
    }
}