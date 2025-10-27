package org.example.metamapa.agregador.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.models.entidades.Categoria;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.service.implementacion.DuplicacionService;
import org.example.metamapa.agregador.service.implementacion.NormalizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class limpiarYRecategorizar {

    @Autowired
    private IRepositorioHechos hechosRepo;
    @Autowired
    private DuplicacionService duplicacionService;
    @Autowired
    private NormalizacionService normalizacionService;

    @Scheduled(cron = "0 0 3 * * *") // todos los días a las 3 AM
    public void limpiarYReetiquetarHechos() {
        log.info("Iniciando limpieza nocturna (deduplicación + recategorización)");

        LocalDateTime desde = LocalDateTime.now().minusDays(7);
        List<Hecho> recientes = hechosRepo.findByFechaCargaAfter(desde);
        log.info("Se analizarán {} hechos recientes...", recientes.size());

        List<Hecho> sinDuplicados = duplicacionService.eliminarHechosRepetidos(recientes);

        AtomicInteger recategorizados = new AtomicInteger(0);

        sinDuplicados.stream()
                .filter(h -> h.getCategoria() == null)
                .forEach(h -> {
                    Categoria nuevaCat = normalizacionService.normalizarCategoriaDesdeTexto(
                            h.getTitulo(), h.getDescripcion());
                    if (nuevaCat != null) {
                        h.setCategoria(nuevaCat);
                        recategorizados.incrementAndGet();
                    }
                });

        hechosRepo.saveAll(sinDuplicados);
        log.info("Limpieza completada: {} hechos actualizados, {} recategorizados.",
                sinDuplicados.size(), recategorizados.get());
    }
}

