package org.example.metamapa.service.implementacion;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.adapters.IAdapterMetamapa;
import org.example.metamapa.exceptions.ExcepcionConexionMetamapa;
import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.models.dtos.HechoDTO_IN;
import org.example.metamapa.models.entidades.EstadoConsulta;
import org.example.metamapa.models.entidades.EstadoLoader;
import org.example.metamapa.models.repositorio.IEstadoConsultaRepositorio;
import org.example.metamapa.service.ICargaMetamapaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CargaMetamapaService implements ICargaMetamapaService {

    private final IAdapterMetamapa adapter;
    private final IEstadoConsultaRepositorio estadoRepo;

    @Value("${loader.id}")
    private String loaderId;


    @PostConstruct
    public void validarLoaderIdUnico() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            if (e.getEstado() != EstadoLoader.FINALIZADO) {
                throw new IllegalStateException(
                        "Ya existe un loader activo con ID '" + loaderId + "'. " +
                                "Detenelo antes de iniciar una nueva instancia."
                );
            }
        });
        log.info("Loader " + loaderId + " iniciado correctamente");
    }


    @Override
    public List<HechoDTO> obtenerHechos() {
        LocalDateTime ultimaConsulta = estadoRepo.findById(loaderId)
                .filter(e -> "OK".equals(e.getEstado()))
                .map(EstadoConsulta::getUltimaConsulta)
                .orElse(null);

        List<HechoDTO> hechosListos;
        try {
            List<HechoDTO_IN> hechosEntrantes = adapter.obtenerHechos(ultimaConsulta);
            hechosListos = hechosEntrantes.stream()
                    .map(this::mapearHecho)
                    .toList();
            registrarEstado(hechosListos, EstadoLoader.OK);
        } catch (ExcepcionConexionMetamapa e) {
            registrarEstado(Collections.emptyList(), EstadoLoader.ERROR);
            throw e;
        }


        return hechosListos;
    }

    private void registrarEstado(List<HechoDTO> hechosListos, EstadoLoader estado) {
        EstadoConsulta estadoConsulta = new EstadoConsulta(
                loaderId,
                LocalDateTime.now(),
                hechosListos.size(),
                estado
        );
        estadoRepo.save(estadoConsulta);
    }

    @PreDestroy
    public void marcarFinalizado() {
        estadoRepo.findById(loaderId).ifPresent(e -> {
            e.setEstado(EstadoLoader.FINALIZADO);
            e.setUltimaConsulta(LocalDateTime.now());
            estadoRepo.save(e);
            System.out.println("Loader " + loaderId + " marcado como FINALIZADO");
        });
    }

    private HechoDTO mapearHecho(HechoDTO_IN in) {
        return HechoDTO.builder()
                .titulo(in.getTitulo())
                .descripcion(in.getDescripcion())
                .categoria(in.getCategoria())
                .latitud(in.getLatitud())
                .longitud(in.getLongitud())
                .fechaAcontecimiento(in.getFechaAcontecimiento())
                .etiqueta(in.getEtiqueta())
                .contribuyenteID(in.getContribuyenteID())
                .archivosMultimedia(in.getArchivosMultimedia())
                .sinCategorizar(in.getSinCategorizar())
                .fechaAcontecimientoPosta(in.getFechaAcontecimientoPosta())
                .build();
    }
}
