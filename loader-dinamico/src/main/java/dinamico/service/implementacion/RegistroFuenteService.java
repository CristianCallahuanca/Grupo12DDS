package dinamico.service.implementacion;

import dinamico.models.dtos.output.FuenteDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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
        String endpoint = urlAgregador + "/registrar";

        log.info("Anunciando fuente al agregador en {}", endpoint);

        try {
            webClient.post()
                    .uri("/registrarse")
                    .bodyValue(dto)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            log.info("Loader registrado exitosamente en el Agregador");
        } catch (Exception e) {
            log.error("Error al registrar el loader en el Agregador", e);
            throw new IllegalStateException("Fallo al anunciarse al Agregador, abortando arranque");
        }
    }
}

