package org.example.metamapa.loaderdemo.service.implementaciones;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.loaderdemo.infraestructura.adapters.IAdapterFuenteDemo;
import org.example.metamapa.loaderdemo.models.entidades.EstadoInstancia;
import org.example.metamapa.loaderdemo.models.entidades.EstadoLoaderDemo;
import org.example.metamapa.loaderdemo.models.entidades.HechoCrudo;
import org.example.metamapa.loaderdemo.models.repositorio.IEstadoLoaderDemoRepositorio;
import org.example.metamapa.loaderdemo.models.repositorio.IRepositorioHechos;
import org.example.metamapa.loaderdemo.service.ICargadorHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CargadorHechosService implements ICargadorHechosService {

    private final IAdapterFuenteDemo adapter;
    private final IRepositorioHechos repositorio;
    private final IEstadoLoaderDemoRepositorio estadoRepo;


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

    @Override
    public void cargarSiguienteHecho() {
        adapter.obtenerSiguienteHecho()
                .map(this::mapearADominio)
                .ifPresent(repositorio::save);
    }

    private HechoCrudo mapearADominio(Map<String, Object> datos) {
        LocalDateTime fechaHecho = null;
        try {

            fechaHecho = LocalDateTime.parse(datos.get("fecha").toString(),
                    java.time.format.DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            log.warn("No se pudo parsear la fecha '{}', usando fecha actual.", datos.get("fecha"));
            fechaHecho = LocalDateTime.now();
        }

        return HechoCrudo.builder()
                .loaderId(loaderId)
                .titulo((String) datos.get("titulo"))
                .descripcion((String) datos.get("descripcion"))
                .categoria((String) datos.get("categoria"))
                .latitud(Double.valueOf(datos.get("latitud").toString()))
                .longitud(Double.valueOf(datos.get("longitud").toString()))
                .etiqueta((String) datos.get("etiqueta"))
                .fecha(fechaHecho.toLocalDate())
                .origen((String) datos.get("origen"))
                .fechaIngesta(LocalDate.now())
                .enviado(false)
                .build();
    }

}
