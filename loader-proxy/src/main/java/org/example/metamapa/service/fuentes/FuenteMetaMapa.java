package org.example.metamapa.service.fuentes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.example.metamapa.models.entidades.HechoCrudo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.stream.Collectors;


public class FuenteMetaMapa  implements IFuenteProxy {

    @Setter
    @Getter
    private String urlBase;

    @Override
    public List<HechoCrudo> cargarHechosExternos() {
        return obtenerHechosDeOtraInstancia(Map.of(), "");
    }

    /** GET /hechos (con filtros opcionales) */
    public List<HechoCrudo> obtenerHechosDeOtraInstancia(Map<String, String> filtros, String sufijoUrl) {
        String endpoint = urlBase + sufijoUrl + "/hechos";
        if (!filtros.isEmpty()) {
            String qs = filtros.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));
            endpoint += "?" + qs;
        }
        return fetchHechos(endpoint);
    }

    /** GET /colecciones/:id/hechos */
    public List<HechoCrudo> obtenerHechosDeColeccion(String idColeccion, Map<String, String> filtros, String sufijoUrl) {
        String endpoint = urlBase + sufijoUrl + "/colecciones/" + idColeccion + "/hechos";
        if (!filtros.isEmpty()) {
            String qs = filtros.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));
            endpoint += "?" + qs;
        }
        return fetchHechos(endpoint);
    }

    /** POST /solicitudes */
    public void enviarSolicitudDeEliminacion(String idHecho, String sufijoUrl) {
        String endpoint = urlBase + sufijoUrl + "/solicitudes";
        postJson(endpoint, new Gson().toJson(idHecho));
    }

    private List<HechoCrudo> fetchHechos(String endpoint) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(endpoint)).GET().build();

        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                Type listType = new TypeToken<List<HechoCrudo>>(){}.getType();
                return new Gson().fromJson(res.body(), listType);
            }
            System.out.println("Error GET " + endpoint + " -> " + res.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void postJson(String endpoint, String json) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("POST " + endpoint + " -> " + res.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }
}
