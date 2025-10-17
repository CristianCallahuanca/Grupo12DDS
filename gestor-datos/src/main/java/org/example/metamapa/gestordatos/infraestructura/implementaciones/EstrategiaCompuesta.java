package org.example.metamapa.gestordatos.infraestructura.implementaciones;

import java.util.List;

public class EstrategiaCompuesta implements IEstrategiaDeteccion {

    private final List<IEstrategiaDeteccion> estrategias;

    public EstrategiaCompuesta(List<IEstrategiaDeteccion> estrategias) {
        this.estrategias = estrategias;
    }

    @Override
    public boolean detectar(String texto) {
        return estrategias.stream().anyMatch(e -> e.detectar(texto));
    }
}
