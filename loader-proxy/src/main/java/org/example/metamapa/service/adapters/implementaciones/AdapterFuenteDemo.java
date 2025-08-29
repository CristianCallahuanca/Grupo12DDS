package org.example.metamapa.service.adapters.implementaciones;

import org.example.metamapa.models.entidades.HechoCrudo;
import org.example.metamapa.service.adapters.IAdapaterFuenteProxy;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdapterFuenteDemo implements IAdapaterFuenteProxy {

    private Conexion conexion;
    private URL url;
    private LocalDateTime ultimaConsulta;

    public AdapterFuenteDemo(Conexion conexion, URL url) {
        this.conexion = conexion;
        this.url = url;
        this.ultimaConsulta = LocalDateTime.now();
    }

    @Override
    public List<HechoCrudo> conseguirHechos() {
        List<HechoCrudo> hechos = new ArrayList<>();
        Map<String, Object> data;
        do {
            data = conexion.siguienteHecho(url, ultimaConsulta);
            if (data != null) {
                hechos.add(this.mapearAHecho(data));
            }
        } while (data != null);

        this.ultimaConsulta = LocalDateTime.now();
        return hechos;
    }

    //Faltan atributos del hecho? Por ejemplo fecha de acontecimiento
    private HechoCrudo mapearAHecho(Map<String, Object> data) {
        String titulo = (String) data.get("titulo");
        String descripcion = (String) data.get("descripcion");
        String categoria = (String) data.get("categoria");

        String latitud = (String) data.get("latitud");
        String longitud = (String) data.get("longitud");

        // Definís el formato que quieras
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Convertís a String
        String fechaActual = LocalDateTime.now().format(formatter);

        HechoCrudo unHecho = new HechoCrudo(
                titulo,
                descripcion,
                categoria,
                latitud,
                longitud,
                fechaActual
                //"demo"
        );

        return unHecho;
    }


}
