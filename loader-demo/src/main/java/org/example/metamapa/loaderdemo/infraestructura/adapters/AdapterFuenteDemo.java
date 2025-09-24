package org.example.metamapa.loaderdemo.infraestructura.adapters;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.infraestructura.externos.Conexion;
import org.example.metamapa.loaderdemo.models.entidades.HechoCrudo;
import org.example.metamapa.loaderdemo.models.repositorio.IRepositorioHechos;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class AdapterFuenteDemo implements IAdapterFuenteDemo {

    private final Conexion conexion;
    private final URL url;
    private final IRepositorioHechos repositorio;
    private LocalDateTime fechaUltimaConsulta = LocalDateTime.now().minusHours(1); // ejemplo inicial

    @Override
    public Optional<HechoCrudo> obtenerSiguienteHecho() {
        Map<String, Object> datos = conexion.siguienteHecho(url, fechaUltimaConsulta);
        if (datos == null) return Optional.empty();

        fechaUltimaConsulta = LocalDateTime.now(); // avanzamos "cursor"

        HechoCrudo hecho = mapearADominio(datos);
        repositorio.save(hecho); // guardamos en base

        return Optional.of(hecho);
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
                .build();
    }
}
