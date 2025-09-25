package org.example.metamapa.loaderdemo.infraestructura.adapters;

import org.example.metamapa.loaderdemo.models.entidades.HechoCrudo;

import java.util.Map;
import java.util.Optional;

public interface IAdapterFuenteDemo {
    public Optional<Map<String, Object>> obtenerSiguienteHecho();
}
