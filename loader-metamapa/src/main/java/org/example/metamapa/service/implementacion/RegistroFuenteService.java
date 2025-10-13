package org.example.metamapa.service.implementacion;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.models.dtos.FuenteDTO;
import org.example.metamapa.models.entidades.EstadoLoader;
import org.example.metamapa.models.repositorio.IEstadoConsultaRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RegistroFuenteService {

    private final IEstadoConsultaRepositorio estadoRepo;
    private final WebClient webClient = WebClient.create();

    public RegistroFuenteService(IEstadoConsultaRepositorio estadoRepo){
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
            if (e.getEstado() != EstadoLoader.FINALIZADO) {
                throw new IllegalStateException(
                        "Ya existe un loader activo con ID '" + loaderId + "'. " +
                                "Detenelo antes de iniciar una nueva instancia."
                );
            }
        });
        log.info("Loader " + loaderId + " iniciado correctamente");
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

    @PreDestroy
    public void marcarFinalizado() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            e.setEstado(EstadoLoader.FINALIZADO);
            e.setUltimaConsulta(LocalDateTime.now());
            estadoRepo.save(e);
            System.out.println("Loader " + loaderId + " marcado como FINALIZADO");
        });
    }
}

