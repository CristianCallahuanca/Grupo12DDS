package org.example.metamapa.Controladores;

import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GatewayController {

    private final RestTemplate restTemplate;
    private final Map<String, String> servicios;

    public GatewayController() {
        this.restTemplate = new RestTemplate();

        // Evitamos que RestTemplate lance excepciones en 4xx y 5xx
        this.restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // No hacemos nada, manejamos los errores manualmente
            }
        });

        // Mapear módulo -> URL base
        servicios = new HashMap<>();
        servicios.put("gestordatos", "http://localhost:8500");
        servicios.put("fuenteDinamica", "http://localhost:8102");
        servicios.put("agregador", "http://localhost:8200");
        servicios.put("estadisticas", "http://localhost:8600");
        servicios.put("fuenteDemo", "http://localhost:8700");
        servicios.put("fuenteEstatica", "http://localhost:8101");
        servicios.put("fuenteMetamapa", "http://localhost:8103");
    }

    @RequestMapping("/{modulo}/**")
    public ResponseEntity<String> proxy(HttpServletRequest request, @PathVariable String modulo) {
        String baseUrl = servicios.get(modulo);

        if (baseUrl == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Módulo desconocido: " + modulo + "\"}");
        }

        // Obtenemos el path después del módulo
        String path = request.getRequestURI().substring(("/" + modulo).length());

        String url = baseUrl + "/" + modulo + path;

        //System.out.println("Proxy -> " + url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return ResponseEntity.status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("{\"error\": \"No se pudo conectar al microservicio\", " +
                            "\"detalle\": \"" + e.getMessage() + "\"}");
        }
    }
}
