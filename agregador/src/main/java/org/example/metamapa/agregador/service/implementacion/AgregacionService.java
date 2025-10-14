package org.example.metamapa.agregador.service.implementacion;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.Fuente;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IFuenteRepository;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.service.IAgregacionService;
import org.example.metamapa.agregador.service.IDuplicacionService;
import org.example.metamapa.agregador.service.INormalizacionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AgregacionService implements IAgregacionService{

    private final INormalizacionService normalizacionService;
    private final IDuplicacionService duplicacionService;
    private final IRepositorioHechos hechosRepository;
    private final IFuenteRepository fuenteRepository;
    private final WebClient webClient;

    @Value("${agregador.timeout.segundos}")
    private int timeoutSegundos;

    public AgregacionService(
            INormalizacionService normalizacionService,
            IDuplicacionService duplicacionService,
            IRepositorioHechos hechosRepository,
            IFuenteRepository fuenteRepository
    ) {
        this.normalizacionService = normalizacionService;
        this.duplicacionService = duplicacionService;
        this.hechosRepository = hechosRepository;
        this.fuenteRepository = fuenteRepository;
        this.webClient = WebClient.create();
    }

    @Transactional
    public void integrarHechosFuentes() {
        log.info("=== Iniciando proceso de integración de hechos ===");
        long inicio = System.currentTimeMillis();

        List<HechoDTO_IN> hechosDTO = obtenerHechosDeFuentesParalelo();
        int total = hechosDTO.size();
        log.info("Se recibieron {} hechos desde las fuentes registradas.", total);

        List<Hecho> hechosNormalizados =
                (hechosDTO.size() > 5000 ? hechosDTO.parallelStream() : hechosDTO.stream())
                        .map(dto -> {
                            try {
                                return normalizacionService.normalizarHecho(dto);
                            } catch (Exception e) {
                                log.warn("Error normalizando '{}': {}", dto.getTitulo(), e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();

        List<Hecho> hechosFiltrados = duplicacionService.eliminarHechosRepetidos(hechosNormalizados);
        int eliminados = hechosNormalizados.size() - hechosFiltrados.size();
        log.info("Filtrados {} hechos duplicados entre fuentes.", eliminados);

        try {
            hechosRepository.saveAll(hechosFiltrados);
            log.info("Guardados {} hechos nuevos.", hechosFiltrados.size());
        } catch (Exception e) {
            log.error("Error al guardar hechos: {}", e.getMessage());
        }

        long fin = System.currentTimeMillis();
        log.info("Integración completada: {} hechos almacenados en {} ms (~{} min).",
                hechosNormalizados.size(),
                (fin - inicio),
                String.format("%.2f", (fin - inicio) / 60000.0));
    }



    public List<HechoDTO_IN> obtenerHechosDeFuentesParalelo() {
        List<Fuente> fuentes = fuenteRepository.findAll();

        Flux<HechoDTO_IN> hechosFlux = Flux.fromIterable(fuentes)
                .flatMap(fuente -> obtenerHechosFuenteAsync(fuente) // cada loader en paralelo TODO: QUIZAS puedo limitar la cantidad de hilos
                        .onErrorResume(e -> {
                            log.warn("Error con fuente {}: {}", fuente.getNombre(), e.getMessage());
                            return Mono.empty(); // si falla, ignora y sigue
                        }))
                .flatMap(Flux::fromIterable); // convierte cada List<HechoDTO_IN> a stream de HechoDTO_IN

        List<HechoDTO_IN> hechosTotales = hechosFlux.collectList().block(); // bloquea al final
        log.info("========== RESUMEN DE INTEGRACIÓN ==========");
        fuentes.forEach(f ->
                log.info("Fuente: {} ({}) -> BaseURL: {}", f.getNombre(), f.getTipo(), f.getBaseUrl())
        );
        log.info("Total de hechos integrados: {} desde {} fuentes activas",
                hechosTotales.size(), fuentes.size());
        log.info("============================================");
        return hechosTotales;
    }


    private Mono<List<HechoDTO_IN>> obtenerHechosFuenteAsync(Fuente fuente) {
        String url = fuente.getBaseUrl() + "/hechos";
        log.info("Consultando hechos de: {}", fuente.getNombre());

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        resp -> Mono.error(new RuntimeException("Error 4xx en " + fuente.getNombre())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> Mono.error(new RuntimeException("Error 5xx en " + fuente.getNombre())))
                .bodyToFlux(HechoDTO_IN.class)
                .collectList()
                .timeout(Duration.ofSeconds(timeoutSegundos))
                .doOnSuccess(list -> log.info("Exito! {} -> {} hechos", fuente.getNombre(), list.size()))
                .doOnError(e -> log.error("Falló {}: {}", fuente.getNombre(), e.getMessage()));
    }


}


/*DEJO ALGUNOS AGREGADOS QUE QUIZAS INTERESEN PARA EL USO MAS ABARCATIVO DE ESTA CLASE o mas ESPECIFICO


SI lo que quiero es tratar un tiempo maximo a las peticiones totales y no solo a la fuente que mas tarda
para optimizar tiempos de respuesta

 @Value("${agregador.timeout.segundos:10}")
private int timeoutSegundos;
...
.timeout(Duration.ofSeconds(timeoutSegundos))


en el .yml

agregador:
  timeout:
    segundos: 10


Si por alguna razon puedo evaluar muchisimas fuentes y quiero gestionas mejor las peticiones activas seria lo mejor
tener un metodo para acordar fuentes que tenemos persistidas y no estan activas con este metodo

private boolean fuenteDisponible(String baseUrl) {
    try {
        webClient.get()
            .uri(baseUrl + "/status")
            .retrieve()
            .toBodilessEntity()
            .block();
        return true;
    } catch (Exception e) {
        log.warn("Fuente inactiva: {}", baseUrl);
        return false;
    }
}

Y ese metodo usarlo asi aca

List<Fuente> activas = fuenteRepository.findAll().stream()
    .filter(f -> fuenteDisponible(f.getBaseUrl()))
    .toList();

Flux.fromIterable(activas)
    .flatMap(this::obtenerHechosFuenteAsync)
    ...


 */



