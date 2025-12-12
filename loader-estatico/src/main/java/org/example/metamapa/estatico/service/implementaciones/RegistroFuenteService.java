package org.example.metamapa.estatico.service.implementaciones;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.models.dtos.FuenteDTO;
import org.example.metamapa.estatico.models.entidades.EstadoInstancia;
import org.example.metamapa.estatico.models.entidades.EstadoLoaderEstatico;
import org.example.metamapa.estatico.models.repositorios.IEstadoLoaderEstaticoRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RegistroFuenteService {

    private final IEstadoLoaderEstaticoRepositorio estadoRepo;
    private final WebClient webClient = WebClient.create();
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
