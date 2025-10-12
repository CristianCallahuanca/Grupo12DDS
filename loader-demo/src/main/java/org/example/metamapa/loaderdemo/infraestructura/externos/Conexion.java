package org.example.metamapa.loaderdemo.infraestructura.externos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Slf4j
@Component
public class Conexion {

    private final WebClient webClient;
    private String token;

    public Conexion(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api-ddsi.disilab.ar/public")
                .build();
    }

    /**
     * Autentica contra la API de la cátedra y guarda el token obtenido.
     */
    private void autenticar() {
        try {
            log.info("Iniciando autenticación...");

            Map<String, Object> response = webClient.post()
                    .uri("/api/login")
                    .bodyValue(Map.of(
                            "email", "ddsi@gmail.com",
                            "password", "ddsi2025*"
                    ))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || response.get("data") == null) {
                throw new RuntimeException("Respuesta nula o inválida al autenticar");
            }

            Map<String, Object> data = (Map<String, Object>) response.get("data");
            this.token = (String) data.get("access_token");

            log.info("Token obtenido correctamente.");

        } catch (WebClientResponseException e) {
            log.error("Error API Auth {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Fallo de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Excepción inesperada durante la autenticación", e);
            throw new RuntimeException("Error inesperado en autenticación", e);
        }
    }

    /**
     * Consulta los desastres naturales desde la API externa.
     *
     * @param page     número de página
     * @param perPage  cantidad de registros por página
     * @return mapa con la respuesta de la API o null si hubo error
     */
    public Map<String, Object> obtenerDesastres(int page, int perPage) {
        try {
            if (token == null) {
                autenticar();
            }

            log.info("Solicitando desastres (page={}, per_page={})", page, perPage);

            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/desastres")
                            .queryParam("page", page)
                            .queryParam("per_page", perPage)
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.debug("Respuesta recibida correctamente para página {}", page);
            return response;

        } catch (WebClientResponseException e) {
            log.error("Error API {} - {}", e.getStatusCode(), e.getResponseBodyAsString());

            // Token expirado: forzamos reautenticación
            if (e.getStatusCode().value() == 401) {
                log.warn("Token expirado o inválido. Reintentando autenticación...");
                token = null;
            }
            return null;

        } catch (Exception e) {
            log.error("Excepción inesperada durante la consulta a la API", e);
            return null;
        }
    }
}
