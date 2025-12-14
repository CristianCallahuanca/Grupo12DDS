package org.example.metamapa.estatico.service.implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.adapters.IAdapterFileServer;
import org.example.metamapa.estatico.models.entidades.FuenteEstatica;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;
import org.example.metamapa.estatico.models.repositorios.IFuenteEstaticaRepositorio;
import org.example.metamapa.estatico.models.repositorios.IRepositorioHechos;
import org.example.metamapa.estatico.service.IProcesadorCsvService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProcesadorCsvService implements IProcesadorCsvService {

    private final IAdapterFileServer adapter;
    private final IFuenteEstaticaRepositorio fuenteRepo;
    private final IRepositorioHechos repoHechos;

    @Value("${loader.self.id}")
    private String loaderId;

    public void procesarFuenteDesdeBytes(
            FuenteEstatica fuente,
            byte[] contenido,
            String nombreArchivo
    ) throws IOException {

        log.info("Procesando fuente '{}' desde archivo en memoria",
                fuente.getNombreFuente());

        String hashNuevo = HashUtil.calcularSHA256(contenido);
        String hashAnterior = fuente.getHashUltimoProcesado();

        if (hashAnterior != null && hashAnterior.equals(hashNuevo)) {
            log.info("Fuente '{}' sin cambios. Se omite.", fuente.getNombreFuente());
            fuente.setPendienteProcesar(false);
            fuenteRepo.save(fuente);
            return;
        }

        List<HechoCrudo> hechos =
                adapter.parsearArchivo(nombreArchivo, contenido);

        for (HechoCrudo hecho : hechos) {
            hecho.setLoaderId(loaderId);
            hecho.setFuenteOrigen(fuente.getNombreFuente());
        }

        repoHechos.saveAll(hechos);

        fuente.setHashUltimoProcesado(hashNuevo);
        fuente.setFechaUltimoProcesamiento(LocalDateTime.now());
        fuente.setPendienteProcesar(false);

        fuenteRepo.save(fuente);

        log.info("Procesamiento completo de '{}' ({} hechos)",
                fuente.getNombreFuente(), hechos.size());
    }

    public ProcesadorCsvService(
            IAdapterFileServer adapter,
            IFuenteEstaticaRepositorio fuenteRepo,
            IRepositorioHechos repoHechos
    ) {
        this.adapter = adapter;
        this.fuenteRepo = fuenteRepo;
        this.repoHechos = repoHechos;
    }

    @Override
    public void procesarFuentesPendientes() {

        List<FuenteEstatica> pendientes =
                fuenteRepo.findByPendienteProcesarTrue();

        if (pendientes.isEmpty()) {
            log.info("No hay fuentes estáticas pendientes de procesar.");
            return;
        }

        log.info("Encontradas {} fuentes pendientes de procesar", pendientes.size());

        for (FuenteEstatica fuente : pendientes) {
            try {
                procesarFuente(fuente);
            } catch (Exception e) {
                log.error("Error procesando la fuente {}: {}", fuente.getNombreFuente(),
                        e.getMessage());
            }
        }
    }

    public void procesarFuente(FuenteEstatica fuente) throws IOException {

        /*Path ruta = Path.of(fuente.getRutaArchivoCsv());
        log.info("Procesando fuente '{}' desde archivo {}",
                fuente.getNombreFuente(), ruta);

        byte[] contenido = adapter.leerArchivo(ruta);

        String hashNuevo = HashUtil.calcularSHA256(contenido);
        String hashAnterior = fuente.getHashUltimoProcesado();

        if (hashAnterior != null && hashAnterior.equals(hashNuevo)) {
            log.info("Fuente '{}' sin cambios. Se omite.", fuente.getNombreFuente());
            fuente.setPendienteProcesar(false);
            fuenteRepo.save(fuente);
            return;
        }

        List<HechoCrudo> hechos = adapter.parsearArchivo(ruta.getFileName().toString(), contenido);

        if (hechos.isEmpty()) {
            log.warn("La fuente '{}' no generó hechos válidos.", fuente.getNombreFuente());
        }

        for (HechoCrudo hecho : hechos) {
            hecho.setLoaderId(loaderId);
            hecho.setFuenteOrigen(fuente.getNombreFuente());
        }

        repoHechos.saveAll(hechos);

        fuente.setHashUltimoProcesado(hashNuevo);
        fuente.setFechaUltimoProcesamiento(LocalDateTime.now());
        fuente.setPendienteProcesar(false);

        fuenteRepo.save(fuente);

        log.info("Procesamiento completo de '{}' ({} hechos)",
                fuente.getNombreFuente(), hechos.size());*/
    }
}

