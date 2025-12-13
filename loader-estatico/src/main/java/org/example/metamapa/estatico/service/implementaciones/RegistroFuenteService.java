package org.example.metamapa.estatico.service.implementaciones;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.models.dtos.FuenteDTO;
import org.example.metamapa.estatico.models.entidades.EstadoInstancia;
import org.example.metamapa.estatico.models.entidades.EstadoLoaderEstatico;
import org.example.metamapa.estatico.models.repositorios.IEstadoLoaderEstaticoRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class RegistroFuenteService {

    private final IEstadoLoaderEstaticoRepositorio estadoRepo;
    private final WebClient webClient = WebClient.create();
    private final AtomicBoolean registrado = new AtomicBoolean(false);

    public RegistroFuenteService(IEstadoLoaderEstaticoRepositorio estadoRepo) {
        this.estadoRepo = estadoRepo;
    }

    @Value("${loader.self.nombreFuente}")
    private String nombreFuente;

    @Value("${loader.self.tipo}")
    private String tipoFuente;

    @Value("${loader.self.baseUrl}")
    private String baseUrl;

    @Value("${agregador.baseUrl}")
    private String urlAgregador;

    @Value("${loader.self.id}")
    private String loaderId;

    // 1) Mantengo tu validación de instancia única
    @PostConstruct
    public void validarLoaderIdUnico() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            if (e.getEstado() == EstadoInstancia.ACTIVO) {
                throw new IllegalStateException(
                        "Ya existe un loader-estático activo con ID '" + loaderId + "'. " +
                                "Detenelo antes de iniciar una nueva instancia."
                );
            }
        });

        estadoRepo.save(
                EstadoLoaderEstatico.builder()
                        .loaderId(loaderId)
                        .estado(EstadoInstancia.ACTIVO)
                        .build()
        );

        log.info("LoaderEstático iniciado correctamente con ID '{}'", loaderId);
    }

    @PreDestroy
    public void marcarFinalizado() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            e.setEstado(EstadoInstancia.FINALIZADO);
            estadoRepo.save(e);
            log.info("LoaderEstático '{}' marcado como FINALIZADO", loaderId);
        });
    }

    // 2) Intento inicial en el arranque (no aborto si falla)
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
