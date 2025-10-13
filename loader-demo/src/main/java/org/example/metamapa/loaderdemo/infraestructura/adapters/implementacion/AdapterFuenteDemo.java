package org.example.metamapa.loaderdemo.infraestructura.adapters.implementacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.loaderdemo.infraestructura.adapters.IAdapterFuenteDemo;
import org.example.metamapa.loaderdemo.infraestructura.externos.Conexion;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdapterFuenteDemo implements IAdapterFuenteDemo {

    private final Conexion conexion;
    private final ObjectMapper mapper = new ObjectMapper();

    private int paginaActual;
    private final int tamanioPagina = 1;
    private LocalDateTime fechaUltimaConsulta = LocalDateTime.now().minusHours(1);
    private final File archivoEstado = new File("estado_loader_demo.json");

    @PostConstruct
    public void inicializarPagina() {
        this.paginaActual = cargarPaginaActual();
        log.info("Página inicial cargada: {}", paginaActual);
    }

    @Override
    public Optional<Map<String, Object>> obtenerSiguienteHecho() {
        Map<String, Object> respuesta = conexion.obtenerDesastres(paginaActual, tamanioPagina);

        if (respuesta == null || !respuesta.containsKey("data")) {
            log.info("No se encontraron datos en la respuesta (página {}).", paginaActual);
            return Optional.empty();
        }

        List<Map<String, Object>> data = (List<Map<String, Object>>) respuesta.get("data");
        if (data.isEmpty()) {
            log.info("Página vacía ({}), no hay nuevos hechos.", paginaActual);
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
        hecho.put("etiqueta", conexion.getEtiquetaFuente());
        hecho.put("origen", conexion.getNombreFuente());

        paginaActual++;
        guardarPaginaActual(paginaActual);
        fechaUltimaConsulta = LocalDateTime.now();

        log.info("Hecho obtenido correctamente. Avanzando a página {}.", paginaActual);
        return Optional.of(hecho);
    }

    private int cargarPaginaActual() {
        try {
            if (!archivoEstado.exists()) {
                log.info("No se encontró archivo de estado, iniciando desde página 1.");
                return 1;
            }
            Map<String, Object> estado = mapper.readValue(archivoEstado, Map.class);
            return (int) estado.getOrDefault("paginaActual", 1);
        } catch (IOException e) {
            log.error("Error al leer archivo de estado, iniciando desde página 1.", e);
            return 1;
        }
    }

    private void guardarPaginaActual(int pagina) {
        try {
            Map<String, Object> estado = new HashMap<>();
            estado.put("paginaActual", pagina);
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivoEstado, estado);
            log.info("Estado guardado (página {}).", pagina);
        } catch (IOException e) {
            log.error("No se pudo guardar el archivo de estado.", e);
        }
    }
}
