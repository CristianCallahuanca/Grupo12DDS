package org.example.metamapa.loaderdemo.service.implementaciones;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.infraestructura.adapters.IAdapterFuenteDemo;
import org.example.metamapa.loaderdemo.models.entidades.HechoCrudo;
import org.example.metamapa.loaderdemo.models.repositorio.IRepositorioHechos;
import org.example.metamapa.loaderdemo.service.ICargadorHechosService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CargadorHechosService implements ICargadorHechosService {

    private final IAdapterFuenteDemo adapter;
    private final IRepositorioHechos repositorio;

    @Override
    public void cargarSiguienteHecho() {
        adapter.obtenerSiguienteHecho()
                .map(this::mapearADominio)
                .ifPresent(repositorio::save);
    }

    private HechoCrudo mapearADominio(Map<String, Object> datos) {
        return HechoCrudo.builder()
                .titulo((String) datos.get("titulo"))
                .descripcion((String) datos.get("descripcion"))
                .categoria((String) datos.get("categoria"))
                .latitud(Double.valueOf(datos.get("latitud").toString()))
                .longitud(Double.valueOf(datos.get("longitud").toString()))
                .etiqueta((String) datos.get("etiqueta"))
                .fecha(LocalDate.parse((String) datos.get("fecha")))
                .fuente("conexion_demo")
                .fechaIngesta(LocalDate.now())
                .enviado(false)
                .build();
    }
}
