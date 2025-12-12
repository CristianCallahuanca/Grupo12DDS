package org.example.metamapa.loaderdemo.infraestructura.adapters.implementacion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.loaderdemo.infraestructura.adapters.IAdapterFuenteDemo;
import org.example.metamapa.loaderdemo.infraestructura.externos.Conexion;
import org.example.metamapa.loaderdemo.models.entidades.FuenteDemo;
import org.example.metamapa.loaderdemo.models.repositorio.IFuenteDemoRepositorio;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdapterFuenteDemo implements IAdapterFuenteDemo {

    private final Conexion conexion;
    private final IFuenteDemoRepositorio fuenteRepo;

    private final int tamanioPagina = 1;

    @Override
    public Optional<Map<String, Object>> obtenerSiguienteHecho(FuenteDemo fuente) {
        int pagina = Optional.ofNullable(fuente.getPaginaActual()).orElse(1);

        Map<String, Object> respuesta = conexion.getJsonConAuth(
                fuente,
                Map.of("page", pagina, "per_page", tamanioPagina)
        );

        if (respuesta == null || !respuesta.containsKey("data")) {
            log.info("No se encontraron datos en la fuente {} (página {}).", fuente.getNombre(), pagina);
            return Optional.empty();
        }

        List<Map<String, Object>> data = (List<Map<String, Object>>) respuesta.get("data");
        if (data.isEmpty()) {
            log.info("Página vacía ({}) para fuente {}, no hay nuevos hechos.", pagina, fuente.getNombre());
            return Optional.empty();
        }

        Map<String, Object> desastre = data.get(0);
        Map<String, Object> hecho = new HashMap<>();
        hecho.put("titulo", desastre.get("titulo"));
        hecho.put("descripcion", desastre.get("descripcion"));
        hecho.put("categoria", desastre.get("categoria"));
        hecho.put("latitud", desastre.get("latitud"));
        hecho.put("longitud", desastre.get("longitud"));
        hecho.put("fecha", desastre.get("fecha_hecho"));

        // Metadatos de la fuente
        String etiqueta = fuente.getEtiquetaDetectada() != null
                ? fuente.getEtiquetaDetectada()
                : "GENERICA";

        hecho.put("etiqueta", etiqueta);
        hecho.put("origen", fuente.getNombre());

        fuente.setPaginaActual(pagina + 1);
        fuente.setUltimaConsulta(LocalDateTime.now());
        fuenteRepo.save(fuente);

        log.info("Hecho obtenido correctamente de {}. Avanzando a página {}.",
                fuente.getNombre(), fuente.getPaginaActual());

        return Optional.of(hecho);
    }


}
