package org.example.metamapa.loaderdemo.service.implementaciones;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.loaderdemo.models.dto.FuenteDTO;
import org.example.metamapa.loaderdemo.models.entidades.EstadoInstancia;
import org.example.metamapa.loaderdemo.models.entidades.EstadoLoaderDemo;
import org.example.metamapa.loaderdemo.models.repositorio.IEstadoLoaderDemoRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RegistroFuenteService {

    private final IEstadoLoaderDemoRepositorio estadoRepo;
    private final WebClient webClient = WebClient.create();

    public RegistroFuenteService(IEstadoLoaderDemoRepositorio estadoRepo){
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

    @Value("${loader.id}")
    private String loaderId;



    @PostConstruct
    public void validarLoaderIdUnico() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            if (e.getEstado() == EstadoInstancia.ACTIVO) {
                throw new IllegalStateException(
                        "Ya existe un loader-demo activo con ID '" + loaderId + "'. " +
                                "Detenelo antes de iniciar una nueva instancia."
                );
            }
        });

        estadoRepo.save(
                EstadoLoaderDemo.builder()
                        .loaderId(loaderId)
                        .fechaInicio(LocalDateTime.now())
                        .ultimaActualizacion(LocalDateTime.now())
                        .estado(EstadoInstancia.ACTIVO)
                        .build()
        );

        log.info("LoaderDemo iniciado correctamente con ID '{}'", loaderId);
    }


    @PreDestroy
    public void marcarFinalizado() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            e.setEstado(EstadoInstancia.FINALIZADO);
            e.setUltimaActualizacion(LocalDateTime.now());
            estadoRepo.save(e);
            log.info("LoaderDemo '{}' marcado como FINALIZADO", loaderId);
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

