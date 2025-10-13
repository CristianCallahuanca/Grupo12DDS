package org.example.metamapa.loaderdemo.service.implementaciones;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.loaderdemo.infraestructura.adapters.IAdapterFuenteDemo;
import org.example.metamapa.loaderdemo.models.entidades.HechoCrudo;
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

    @Value("${loader.id}")
    private String loaderId;

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
