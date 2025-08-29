package org.example.metamapa.models.entidades;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.metamapa.models.entidades.FuenteProxy;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FuenteMetaMapa extends FuenteProxy {

    //List<Hecho> hechos  = new ArrayList<>();
    private String urlBase;
    //SINGLETON
    private static final FuenteMetaMapa instance = new FuenteMetaMapa();

    // Constructor privado
    private FuenteMetaMapa() {}

    public static FuenteMetaMapa getInstancia() {
        return instance;
    }
    //SINGLETON

    //Ejemplos de como se realizaría una busqueda en distintas instancias
    //http://MetaMapa.Argentina.com/hechos
    //http://MetaMapa.Chile.com/hechos
    //http://localhost:7000/hechos

    //urlbase http://MetaMapa  -> ARGENTINA /coleccionHechos
    /*resultados: categoria, fecha_reporte_desde,
     fecha_reporte_hasta,
     fecha_acontecimiento_desde,
    fecha_acontecimiento_hasta, ubicacion.
    */
    /*
    Map<String, String> filtros = new HashMap<>();
    filtros.put("categoria", "tecnologia");
    filtros.put("fecha_acontecimiento_hasta", "2025-12-01-11-12-01");
    */

    // GET /hechos
    public List<HechoCrudo> obtenerHechosDeOtraInstancia(Map<String, String> filtros, String IdURL) {
        String endpoint = urlBase + IdURL + "/hechos";

        // Agregar filtros como queryParams
        if (!filtros.isEmpty()) {
            String queryString = filtros.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())// convierte cada par clave-valor en un string tipo "clave=valor"
                    .collect(Collectors.joining("&"));                             // une todos los pares con '&'
            endpoint += "?" + queryString;
        }

        // Realizar GET a endpoint
        return obtenerHechosDesdeEndpoint(endpoint);
    }

    // GET /colecciones/:id/hechos
    public List<HechoCrudo> obtenerHechosDeColeccionDeOtraInstancia(Map<String, String> filtros, String IDColeccion, String IdURL) {
        String endpoint = urlBase + IdURL + "/colecciones/" + IDColeccion + "/hechos";

        // Agregar filtros como queryParams
        if (!filtros.isEmpty()) {
            String queryString = filtros.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())// convierte cada par clave-valor en un string tipo "clave=valor"
                    .collect(Collectors.joining("&"));                             // une todos los pares con '&'
            endpoint += "?" + queryString;
        }

        return obtenerHechosDesdeEndpoint(endpoint);
    }

    // POST /solicitudes
    public void enviarSolicitudDeEliminacion(String IDHechoAEliminar, String IdURL) {
        String endpoint = urlBase + IdURL + "/solicitudes";

        String cuerpoJson = serializarSolicitud(IDHechoAEliminar);

        hacerPost(endpoint, cuerpoJson);
    }

    private List<HechoCrudo> obtenerHechosDesdeEndpoint(String endpoint) {
        HttpClient client = HttpClient.newHttpClient(); // Crea una instancia del cliente HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))              // Usa la URL del parametro
                .GET()                                  // Especifica que la petición es GET
                .build();

        try {
            //Envía la solicitud y espera una respuesta como String.
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();                 // Usa Gson para convertir el JSON recibido a una lista de objetos Hecho
                Type listType = new TypeToken<List<HechoCrudo>>() {}.getType();
                return gson.fromJson(response.body(), listType);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private String serializarSolicitud(String solicitud) {
        Gson gson = new Gson();
        return gson.toJson(solicitud);
    }


    private void hacerPost(String endpoint, String jsonBody) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))                              // ← a qué URL se envía
                .header("Content-Type", "application/json")       // ← indica que estás enviando JSON
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))    // ← cuerpo del POST en formato JSON
                .build();
        try {
            //Envía la solicitud y espera la respuesta como String.
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Respuesta POST: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
