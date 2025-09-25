package org.example.metamapa.loaderdemo.infraestructura.externos;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class Conexion {
    public Map<String, Object> siguienteHecho(URL url, LocalDateTime fechaUltimaConsulta) {
        // Simulación de una respuesta de ejemplo. En tu TP esto ya viene hecho.
        Map<String, Object> hecho = new HashMap<>();
        hecho.put("titulo", "Incendio en zona norte");
        hecho.put("descripcion", "Fuego en zona boscosa");
        hecho.put("categoria", "Incendio");
        hecho.put("latitud", -34.6037);
        hecho.put("longitud", -58.3816);
        hecho.put("fecha", "2025-09-23");
        hecho.put("etiqueta", "URGENTE");

        return hecho; // Podría devolver `null` si no hay hechos nuevos
    }
} //TODO: ANIDAR A LA API DE EJEMPLO DE ESCOBAR

