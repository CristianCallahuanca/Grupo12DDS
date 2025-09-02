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
        HechoCrudo hecho = new HechoCrudo();

        hecho.setTitulo((String) data.getOrDefault("titulo", "Sin título"));
        hecho.setDescripcion((String) data.getOrDefault("descripcion", "Sin descripción"));
        hecho.setCategoria((String) data.getOrDefault("categoria", "Sin categoría"));
        hecho.setUbicacion((String) data.getOrDefault("ubicacion", "Sin ubicación"));
        hecho.setLatitud((String) data.getOrDefault("latitud", null));
        hecho.setLongitud((String) data.getOrDefault("longitud", null));
        hecho.setEtiqueta((String) data.getOrDefault("etiqueta", null));
        hecho.setContribuyenteID((String) data.getOrDefault("contribuyenteID", null));
        hecho.setArchivosMultimedia((String) data.getOrDefault("archivosMultimedia", null));
        hecho.setId_hecho((String) data.getOrDefault("id_hecho", UUID.randomUUID().toString())); // generar ID si no viene
        hecho.setFechaAcontecimiento((String) data.getOrDefault("fechaAcontecimiento", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        hecho.setFechaCarga(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return hecho;
    }


    private URL crearUrlDemo() {
        try {
            return new URL("http://fuente-demo.fake"); // puede ser cualquier valor
        } catch (Exception e) {
            throw new RuntimeException("URL inválida para fuente demo");
        }
    }
}
