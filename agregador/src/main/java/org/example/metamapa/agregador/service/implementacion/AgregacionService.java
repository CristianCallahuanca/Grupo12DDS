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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AgregacionService implements IAgregacionService{

    private final INormalizacionService normalizacionService;
    private final IDuplicacionService duplicacionService;
    private final IRepositorioHechos hechosRepository;
    private final IFuenteRepository fuenteRepository;
    private final WebClient webClient;

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
        List<HechoDTO_IN> hechosDTO = obtenerHechosDeFuentesParalelo();
        List<Hecho> hechosNormalizados = normalizacionService.normalizarHechos(hechosDTO);
        // List<Hecho> hechosFiltrados = duplicacionService.eliminarHechosRepetidos(hechosNormalizados);
        hechosRepository.saveAll(hechosNormalizados);
        log.info("Integración completada: {} hechos almacenados.", hechosNormalizados.size());
    }



    public List<HechoDTO_IN> obtenerHechosDeFuentesParalelo() {
        List<Fuente> fuentes = fuenteRepository.findAll();

        Flux<HechoDTO_IN> hechosFlux = Flux.fromIterable(fuentes)
                .flatMap(fuente -> obtenerHechosFuenteAsync(fuente) // cada loader en paralelo
                        .onErrorResume(e -> {
                            log.warn("Error con fuente {}: {}", fuente.getNombre(), e.getMessage());
                            return Mono.empty(); // si falla, ignora y sigue
                        }))
                .flatMap(Flux::fromIterable); // convierte cada List<HechoDTO_IN> a stream de HechoDTO_IN

        List<HechoDTO_IN> hechosTotales = hechosFlux.collectList().block(); // bloquea al final
        log.info("Recibidos {} hechos de {} fuentes", hechosTotales.size(), fuentes.size());
        return hechosTotales;
    }


    private Mono<List<HechoDTO_IN>> obtenerHechosFuenteAsync(Fuente fuente) {
        String url = fuente.getBaseUrl() + "/hechos";
        log.info("Consultando hechos de: {}", fuente.getNombre());

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(HechoDTO_IN.class)
                .collectList()
                .timeout(Duration.ofSeconds(10)) // hardcodeado
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



