package org.example.metamapa.loaderdemo.infraestructura.externos;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.loaderdemo.models.entidades.FuenteDemo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Slf4j
@Component
public class Conexion {

    private final WebClient.Builder webClientBuilder;

    public Conexion(WebClient.Builder builder) {
        this.webClientBuilder = builder;
    }

    // --------- USO 1: Validar credenciales al registrar fuente ---------

    public boolean validarAutenticacion(String email, String password, String baseUrl) {
        try {
            String token = login(baseUrl, email, password);
            boolean ok = (token != null && !token.isBlank());
            if (ok) {
                log.info("Autenticación válida en {} para usuario {}", baseUrl, email);
            } else {
                log.warn("Autenticación inválida en {} para usuario {}", baseUrl, email);
            }
            return ok;
        } catch (Exception e) {
            log.error("Error validando autenticación en {}: {}", baseUrl, e.getMessage());
            return false;
        }
    }

    // --------- USO 2: Consumir la API externa con auth dinámica ---------

    /**
     * Realiza un GET autenticado usando los datos de la FuenteDemo:
     * - urlBase   -> host base
     * - pathApi   -> path del recurso (ej: "/api/desastres")
     * - authEmail / authPassword -> credenciales para login
     *
     * queryParams permite enviar cosas como page, per_page, etc.
     */
    public Map<String, Object> getJsonConAuth(FuenteDemo fuente,
                                              Map<String, Object> queryParams) {

        String baseUrl = fuente.getUrlBase();
        String path = fuente.getPathApi();
        String email = fuente.getAuthEmail();
        String password = fuente.getAuthPassword();

        try {
            String token = login(baseUrl, email, password);

            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

            log.info("Invocando {}{} con queryParams={} para fuente {}",
                    baseUrl, path, queryParams, fuente.getNombre());

            return webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path(path);
                        queryParams.forEach(uriBuilder::queryParam);
                        return uriBuilder.build();
                    })
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

        } catch (WebClientResponseException e) {
            log.error("Error API {} en {}{} - {}",
                    e.getStatusCode(), baseUrl, path, e.getResponseBodyAsString());
            return null;

        } catch (Exception e) {
            log.error("Excepción inesperada durante la consulta a {}{}: ",
                    baseUrl, path, e);
            return null;
        }
    }

    // --------- Lógica común: login ---------

    /**
     * Hace POST a /api/login con email/password y devuelve el access_token.
     * Para este TP asumimos que todas las fuentes demo usan este endpoint de login.
     */
    private String login(String baseUrl, String email, String password) {
        try {
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

            log.info("Login contra {} con usuario {}", baseUrl, email);

            Map<String, Object> response = webClient.post()
                    .uri("/api/login") // acá sí aceptamos este contrato "de DDSI" //TODO: Quizas agregarlo a la entidad
                    .bodyValue(Map.of("email", email, "password", password))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || response.get("data") == null) {
                throw new RuntimeException("Respuesta nula o inválida al autenticar en " + baseUrl);
            }

            Map<String, Object> data = (Map<String, Object>) response.get("data");
            String token = (String) data.get("access_token");

            log.info("Token obtenido correctamente para {}.", baseUrl);
            return token;

        } catch (Exception e) {
            log.error("Fallo de autenticación en {}: {}", baseUrl, e.getMessage());
            throw e;
        }
    }
}
