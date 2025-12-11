package org.example.metamapa.adapters.implementacion;

 import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.adapters.IAdapterMetamapa;
import org.example.metamapa.exceptions.ExcepcionConexionMetamapa;
import org.example.metamapa.models.dtos.HechoDTO_IN;
import org.springframework.beans.factory.annotation.Value;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.HttpStatusCode;
 import org.springframework.stereotype.Component;
 import org.springframework.web.reactive.function.client.ClientResponse;
 import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;
 import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AdapterMetamapaV1 implements IAdapterMetamapa {

    private final WebClient.Builder webClientBuilder;

    public AdapterMetamapaV1(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public List<HechoDTO_IN> obtenerHechos(String baseUrl, LocalDateTime fechaDesde) {
        try {
            WebClient webClient = webClientBuilder
                    .baseUrl(baseUrl)
                    .build();

            WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uriBuilder -> {
                var builder = uriBuilder.path("/hechos");
                if (fechaDesde != null) {
                    builder.queryParam(
                            "fecha_reporte_desde",
                            fechaDesde.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    );
                }
                return builder.build();
            });

            HechoDTO_IN[] hechos = request
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, this::manejarError)
                    .bodyToMono(HechoDTO_IN[].class)
                    .block();

            return Arrays.asList(hechos != null ? hechos : new HechoDTO_IN[0]);
        } catch (Exception e) {
            log.error("Excepción al conectar con Metamapa en baseUrl={}", baseUrl, e);
            throw new ExcepcionConexionMetamapa("Fallo en la conexión o procesamiento", e);
        }
    }


    private Mono<Throwable> manejarError(ClientResponse clientResponse) {
        HttpStatusCode statusCode = clientResponse.statusCode();
        log.error("Error HTTP al conectar con Metamapa: {}", statusCode);

        if (statusCode instanceof HttpStatus statusHttp) {
            return Mono.error(new ExcepcionConexionMetamapa("Error HTTP al conectar con Metamapa", statusHttp));
        } else {
            return Mono.error(new ExcepcionConexionMetamapa(
                    "Código HTTP desconocido",
                    new Throwable("Código: " + statusCode)
            ));
        }
    }
}

