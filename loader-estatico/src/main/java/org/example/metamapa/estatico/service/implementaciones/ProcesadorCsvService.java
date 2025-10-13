package org.example.metamapa.estatico.service.implementaciones;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.estatico.adapters.IAdapterFileServer;
import org.example.metamapa.estatico.models.dtos.ArchivoCsv;
import org.example.metamapa.estatico.models.entidades.*;
import org.example.metamapa.estatico.models.repositorios.IEstadoLoaderEstaticoRepositorio;
import org.example.metamapa.estatico.models.repositorios.IRepositorioCSVProcesado;
import org.example.metamapa.estatico.models.repositorios.IRepositorioHechos;
import org.example.metamapa.estatico.service.IProcesadorCsvService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j

public class ProcesadorCsvService implements IProcesadorCsvService {

    private final IAdapterFileServer adapter;
    private final IRepositorioCSVProcesado repositorioCSV;
    private final IRepositorioHechos repositorioHechos;
    private final IEstadoLoaderEstaticoRepositorio estadoRepo;

    @Value("${loader.self.id}")
    private String loaderId;

    public ProcesadorCsvService(IAdapterFileServer adapter,
                                IRepositorioCSVProcesado repositorioCSV,
                                IRepositorioHechos repositorioHechos, IEstadoLoaderEstaticoRepositorio estadoRepo) {
        this.adapter = adapter;
        this.repositorioCSV = repositorioCSV;
        this.repositorioHechos = repositorioHechos;
        this.estadoRepo = estadoRepo;
    }

    @PostConstruct
    public void validarLoaderIdUnico() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            if (e.getEstado() == EstadoInstancia.ACTIVO) {
                throw new IllegalStateException(
                        "Ya existe un loader-estático activo con ID '" + loaderId + "'. " +
                                "Detenelo antes de iniciar una nueva instancia."
                );
            }
        });

        estadoRepo.save(
                EstadoLoaderEstatico.builder()
                        .loaderId(loaderId)
                        .fechaInicio(LocalDateTime.now())
                        .ultimaActualizacion(LocalDateTime.now())
                        .estado(EstadoInstancia.ACTIVO)
                        .build()
        );

        log.info("LoaderEstático iniciado correctamente con ID '{}'", loaderId);
    }

    @PreDestroy
    public void marcarFinalizado() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            e.setEstado(EstadoInstancia.FINALIZADO);
            e.setUltimaActualizacion(LocalDateTime.now());
            estadoRepo.save(e);
            log.info("LoaderEstático '{}' marcado como FINALIZADO", loaderId);
        });
    }



    @Override
    public void procesarArchivosCsv() {
        List<ArchivoCsv> archivos = adapter.obtenerArchivosDisponibles();

        if (archivos.isEmpty()) {
            log.warn("No se encontraron archivos para procesar.");
            return;
        }

        for (ArchivoCsv archivo : archivos) {
            try {
                procesarArchivo(archivo);
            } catch (IOException e) {
                log.error("Error leyendo el archivo {}: {}", archivo.getNombre(), e.getMessage());
            } catch (Exception e) {
                log.error("Error procesando el archivo {}: {}", archivo.getNombre(), e.getMessage());
            }
        }
    }

    private void procesarArchivo(ArchivoCsv archivo) throws IOException {
        String nombre = archivo.getNombre();
        byte[] contenido = archivo.leerComoBytes();
        String hashNuevo = HashUtil.calcularSHA256(contenido);

        if (!debeProcesarse(nombre, hashNuevo)) {
            log.info("Archivo {} ya fue procesado y no cambió. Se omite.", nombre);
            return;
        }

        log.debug("Procesando archivo {} con hash {}", nombre, hashNuevo);

        List<HechoCrudo> hechos = adapter.leerArchivoDesdeBytes(nombre, contenido);

        if (hechos.isEmpty()) {
            log.warn("El archivo {} no generó ningún hecho válido. Se omitirá.", nombre);
            return;
        }

        for (HechoCrudo hecho : hechos) {
            hecho.setLoaderId(loaderId);
        }

        repositorioHechos.saveAll(hechos);
        guardarOActualizarRegistro(nombre, hashNuevo);
        log.info("Archivo {} procesado exitosamente.", nombre);
    }

    private boolean debeProcesarse(String nombreArchivo, String nuevoHash) {
        if (!repositorioCSV.existsById_LoaderIdAndId_NombreArchivo(loaderId, nombreArchivo)) return true;

        String hashAnterior = repositorioCSV.obtenerHashPorNombre(loaderId, nombreArchivo);
        return !nuevoHash.equals(hashAnterior);
    }

    private void guardarOActualizarRegistro(String nombreArchivo, String hash) {
        CsvProcesadoId id = new CsvProcesadoId(loaderId, nombreArchivo);
        CsvProcesado registro = new CsvProcesado(id, hash, LocalDateTime.now());
        repositorioCSV.save(registro);

    }

}

