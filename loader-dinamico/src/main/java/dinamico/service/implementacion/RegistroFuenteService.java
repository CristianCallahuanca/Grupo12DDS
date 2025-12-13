package dinamico.service.implementacion;

import dinamico.models.dtos.output.FuenteDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicBoolean;

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
    private final AtomicBoolean registrado = new AtomicBoolean(false);

    @PostConstruct
    public void anunciarEnArranque() {
        intentarRegistro("arranque");
    }

    @Scheduled(fixedDelayString = "${loader.registro.retryDelayMs:10000}")
    public void retryRegistro() {
        if (!registrado.get()) {
            intentarRegistro("retry");
        }
    }

    private void intentarRegistro(String origen) {
        FuenteDTO dto = new FuenteDTO(nombreFuente, tipoFuente, baseUrl);
        String endpoint = urlAgregador + "/fuentes/registrar";

        log.info("[{}] Intentando registrar loader '{}' en {}", origen, nombreFuente, endpoint);

        try {
            webClient.post()
                    .uri(endpoint)
                    .bodyValue(dto)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            registrado.set(true);
            log.info("Loader '{}' registrado exitosamente en el Agregador", nombreFuente);

        } catch (Exception e) {
            log.warn("No se pudo registrar loader '{}' (se reintentará). Causa: {}",
                    nombreFuente, e.getMessage());
        }
    }
}
