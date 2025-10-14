package org.example.metamapa.agregador.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.service.implementacion.DuplicacionService;
import org.example.metamapa.agregador.service.implementacion.NormalizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LimpiezaNocturnaScheduler {

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

        // Re-etiquetado incremental (solo los sin categoría)
        sinDuplicados.stream()
                .filter(h -> h.getCategoria().equalsIgnoreCase("Sin categoria"))
                .forEach(h -> {
                    String nuevaCat = normalizacionService.normalizarCategoriaDesdeTexto(
                            h.getTitulo(), h.getDescripcion());
                    if (!"Sin categoria".equals(nuevaCat)) {
                        h.setCategoria(nuevaCat);
                    }
                });

        hechosRepo.saveAll(sinDuplicados);
        log.info("Limpieza completada: {} hechos actualizados.", sinDuplicados.size());
    }
}

