package org.example.metamapa.loaderdemo.infraestructura.externos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Conexion {

    private final WebClient webClient;
    private String token;
    private String nombreFuente;
    private String etiquetaFuente;

    @Value("${loaderdemo.fuente.url}")
    private String baseUrl;

    public Conexion(WebClient.Builder builder, @Value("${loaderdemo.fuente.url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.nombreFuente = obtenerDominioBase(baseUrl);
        this.etiquetaFuente = "GENERICA";
        log.info("Conexión inicializada con baseUrl={}", baseUrl);
    }
    public String getNombreFuente() { return nombreFuente; }
    public String getEtiquetaFuente() { return etiquetaFuente; }

    /**
     * Autentica contra la API de la cátedra y guarda el token obtenido.
     */
    private void autenticar() {
        try {
            log.info("Iniciando autenticación...");

            // --- LOGIN NORMAL ---
            Map<String, Object> response = webClient.post()
                    .uri("/api/login")
                    .bodyValue(Map.of("email", "ddsi@gmail.com", "password", "ddsi2025*"))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || response.get("data") == null)
                throw new RuntimeException("Respuesta nula o inválida al autenticar");

            Map<String, Object> data = (Map<String, Object>) response.get("data");
            this.token = (String) data.get("access_token");
            log.info("Token obtenido correctamente.");

            // --- CAPTURAR METADATA SOLO UNA VEZ ---
            try {
                Map<String, Object> docs = WebClient.create()
                        .get()
                        .uri(baseUrl + "/docs?api-docs.json")
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

                if (docs != null) {
                    if (docs.containsKey("info")) {
                        Map<String, Object> info = (Map<String, Object>) docs.get("info");
                        nombreFuente = (String) info.getOrDefault("title", nombreFuente);
                    }
                    if (docs.containsKey("tags")) {
                        List<Map<String, Object>> tags = (List<Map<String, Object>>) docs.get("tags");
                        if (!tags.isEmpty()) {
                            etiquetaFuente = tags.stream()
                                    .filter(t -> {
                                        String name = t.get("name").toString().toLowerCase();
                                        return name.contains("desastre") || name.contains("natural");
                                    })
                                    .map(t -> t.get("name").toString().toUpperCase().replace(" ", "_"))
                                    .findFirst()
                                    .orElse("SIN_TAG");
                        } else {
                            etiquetaFuente = "SIN_TAG";
                        }
                    } else {
                        etiquetaFuente = "SIN_TAG";
                    }


                }
                log.info("Fuente detectada: {} ({})", nombreFuente, etiquetaFuente);
            } catch (Exception e) {
                log.warn("No se pudo obtener metadata de la fuente: {}", e.getMessage());
            }

        } catch (Exception e) {
            log.error("Fallo de autenticación: {}", e.getMessage());
            throw new RuntimeException("Error durante autenticación", e);
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

    private String obtenerDominioBase(String url) {
        try {
            String host = new java.net.URL(url).getHost(); // api-ddsi.disilab.ar
            return host.replace("api-", "").replace("www.", "").toUpperCase();
        } catch (Exception e) {
            return "FUENTE-DESCONOCIDA";
        }
    }
}
