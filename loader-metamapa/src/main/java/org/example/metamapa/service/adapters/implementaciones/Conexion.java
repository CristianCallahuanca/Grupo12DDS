package org.example.metamapa.service.adapters.implementaciones;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Simulación de una API externa stateful, que devuelve hechos uno a uno hasta retornar `null`.
 */
public class Conexion {

    private int contador = 0;
    private final int maxHechos = 3; // devuelve 3 hechos y luego corta

    public Map<String, Object> siguienteHecho(URL url, LocalDateTime fechaUltimaConsulta) {
        if (contador < maxHechos) {
            contador++;
            return Map.of(
                    "titulo", "Hecho demo " + contador,
                    "descripcion", "Descripción del hecho número " + contador,
                    "categoria", "prueba",
                    "latitud", "-34.60" + contador,
                    "longitud", "-58.38" + contador
            );
        } else {
            return null; // fin de hechos nuevos
        }
    }
}
