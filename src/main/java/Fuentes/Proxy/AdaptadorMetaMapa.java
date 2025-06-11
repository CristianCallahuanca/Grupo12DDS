package Fuentes.Proxy;

import AdministracionDeHechos.Hecho;
import SolicitudEliminar.SolicitudEliminar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class AdaptadorMetaMapa {

    private String urlBase;

    public AdaptadorMetaMapa(String urlBase) {
        this.urlBase = urlBase;
    }

    // GET /hechos
    public List<Hecho> obtenerHechosExternos(Map<String, String> filtros) {
        String endpoint = urlBase + "/hechos";

        // Agregar filtros como queryParams
        if (!filtros.isEmpty()) {
            String queryString = filtros.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
            endpoint += "?" + queryString;
        }

        // Realizar GET a endpoint
        return parsearRespuestaDeHechos(endpoint);
    }

    // GET /colecciones/:id/hechos
    public List<Hecho> obtenerHechosDeColeccion(String identificadorColeccion) {
        String endpoint = urlBase + "/colecciones/" + identificadorColeccion + "/hechos";
        return parsearRespuestaDeHechos(endpoint);
    }

    // POST /solicitudes
    public void enviarSolicitudDeEliminacion(SolicitudEliminar solicitud) {
        String endpoint = urlBase + "/solicitudes";

        String cuerpoJson = serializarSolicitud(solicitud);

        hacerPost(endpoint, cuerpoJson);
    }

    private List<Hecho> parsearRespuestaDeHechos(String endpoint) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Hecho>>() {}.getType();
                return gson.fromJson(response.body(), listType);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    private String serializarSolicitud(SolicitudEliminar solicitud) {
        Gson gson = new Gson();
        return gson.toJson(solicitud);
    }


    private void hacerPost(String endpoint, String jsonBody) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Respuesta POST: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}


