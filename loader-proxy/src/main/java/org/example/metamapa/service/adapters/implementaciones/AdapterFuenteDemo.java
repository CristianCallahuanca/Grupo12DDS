package org.example.metamapa.service.adapters.implementaciones;

import org.example.metamapa.models.entidades.HechoCrudo;
import org.example.metamapa.service.adapters.IAdapaterFuenteProxy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component
public class AdapterFuenteDemo implements IAdapaterFuenteProxy {

    private final Conexion conexion = new Conexion(); // clase fake que simula API externa
    private final URL url = crearUrlDemo();           // endpoint de la fuente demo
    private LocalDateTime ultimaConsulta = LocalDateTime.now(); // se actualiza tras cada sync

    @Override
    public List<HechoCrudo> adaptarHechosDesdeFuente() {
        List<HechoCrudo> hechos = new ArrayList<>();
        Map<String, Object> data;

        do {
            data = conexion.siguienteHecho(url, ultimaConsulta);
            if (data != null) {
                hechos.add(mapearAHecho(data));
            }
        } while (data != null);

        ultimaConsulta = LocalDateTime.now();
        return hechos;
    }

    private HechoCrudo mapearAHecho(Map<String, Object> data) {
        String titulo = (String) data.getOrDefault("titulo", "Sin título");
        String descripcion = (String) data.getOrDefault("descripcion", "Sin descripción");
        String categoria = (String) data.getOrDefault("categoria", "Sin categoría");
        String ubicacion = (String) data.getOrDefault("ubicacion", "Sin ubicacion");
        //String latitud = (String) data.getOrDefault("latitud", "0.0");
        //String longitud = (String) data.getOrDefault("longitud", "0.0");

        String fechaActual = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //return new HechoCrudo(titulo, descripcion, categoria, latitud, longitud, fechaActual);
        return new HechoCrudo(titulo, descripcion, categoria, ubicacion, fechaActual);
    }

    private URL crearUrlDemo() {
        try {
            return new URL("http://fuente-demo.fake"); // puede ser cualquier valor
        } catch (Exception e) {
            throw new RuntimeException("URL inválida para fuente demo");
        }
    }
}
