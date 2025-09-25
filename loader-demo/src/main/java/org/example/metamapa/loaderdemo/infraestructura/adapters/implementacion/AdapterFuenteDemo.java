package org.example.metamapa.loaderdemo.infraestructura.adapters.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.infraestructura.adapters.IAdapterFuenteDemo;
import org.example.metamapa.loaderdemo.infraestructura.externos.Conexion;
import org.example.metamapa.loaderdemo.models.entidades.HechoCrudo;
import org.example.metamapa.loaderdemo.models.repositorio.IRepositorioHechos;
import org.springframework.stereotype.Component;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AdapterFuenteDemo implements IAdapterFuenteDemo {

    private final Conexion conexion;
    private URL url; //quizas vaya en properties
    private LocalDateTime fechaUltimaConsulta = LocalDateTime.now().minusHours(1); // ejemplo inicial

    @Override
    public Optional<Map<String, Object>> obtenerSiguienteHecho() {
        Map<String, Object> datos = conexion.siguienteHecho(url, fechaUltimaConsulta);
        if (datos == null) return Optional.empty();

        fechaUltimaConsulta = LocalDateTime.now();
        return Optional.of(datos);
    }

}
