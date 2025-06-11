package Fuentes.Proxy;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Ubicacion;
import SolicitudEliminar.SolicitudEliminar;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdaptadorDemo {

    private Conexion conexion;
    private URL url;
    private LocalDateTime  ultimaConsulta;

    public AdaptadorDemo(Conexion conexion, URL url) {
        this.conexion = conexion;
        this.url = url;
        this.ultimaConsulta = LocalDateTime .now();
    }


    public List<Hecho> obtenerHechos() {
        List<Hecho> hechos = new ArrayList<>();
        Map<String, Object> data;
        do {
            data = conexion.siguienteHecho(url, ultimaConsulta);
            if (data != null) {
                hechos.add(this.mapearAHecho(data));
            }
        } while (data != null);

        this.ultimaConsulta = LocalDateTime .now();
        return hechos;
    }


    private Hecho mapearAHecho(Map<String, Object> data) {
        String titulo = (String) data.get("titulo");
        String descripcion = (String) data.get("descripcion");
        String categoria = (String) data.get("categoria");

        double latitud = data.get("latitud") != null ? (double) data.get("latitud") : 0.0;
        double longitud = data.get("longitud") != null ? (double) data.get("longitud") : 0.0;

        Ubicacion ubicacion = new Ubicacion(latitud, longitud);

        return new Hecho(
                titulo,
                descripcion,
                categoria,
                ubicacion,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "demo"
        );
    }

}

