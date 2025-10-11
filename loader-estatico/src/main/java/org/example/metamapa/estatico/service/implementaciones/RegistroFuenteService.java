package org.example.metamapa.estatico.service.implementaciones;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.models.dtos.FuenteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class RegistroFuenteService {

    @Value("${loader.self.nombreFuente}")
    private String nombreFuente;

    @Value("${loader.self.tipo}")
    private String tipoFuente;

    @Value("${loader.self.baseUrl}")
    private String baseUrl;

    @Value("${agregador.baseUrl}")
    private String urlAgregador;

    private final WebClient webClient = WebClient.create();

    @PostConstruct
    public void anunciarFuenteAlAgregador() {
        FuenteDTO dto = new FuenteDTO(nombreFuente, tipoFuente, baseUrl);
        String endpoint = urlAgregador + "/fuentes/registrar";

        log.info("Anunciando fuente '{}' al Agregador en {}", nombreFuente, endpoint);

        try {
            webClient.post()
                    .uri(endpoint)
                    .bodyValue(dto)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            log.info("Loader '{}' registrado exitosamente en el Agregador", nombreFuente);
        } catch (Exception e) {
            log.error("Error al registrar el loader en el Agregador", e);
            throw new IllegalStateException("Fallo al anunciarse al Agregador, abortando arranque");
        }
    }
}
