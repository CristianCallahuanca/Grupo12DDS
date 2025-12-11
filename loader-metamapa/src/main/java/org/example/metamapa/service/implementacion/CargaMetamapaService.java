package org.example.metamapa.service.implementacion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.adapters.IAdapterMetamapa;
import org.example.metamapa.exceptions.ExcepcionConexionMetamapa;
import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.models.dtos.HechoDTO_IN;
import org.example.metamapa.models.entidades.FuenteMetamapa;
import org.example.metamapa.models.repositorio.IFuenteMetamapaRepository;
import org.example.metamapa.service.ICargaMetamapaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CargaMetamapaService implements ICargaMetamapaService {

    private final IAdapterMetamapa adapter;
    private final IFuenteMetamapaRepository fuenteRepo;


    @Override
    public List<HechoDTO> obtenerHechos() {

        List<FuenteMetamapa> fuentes = fuenteRepo.findByActivaTrue();
        if (fuentes.isEmpty()) {
            log.warn("No hay fuentes Metamapa registradas/activas. Devolviendo lista vacía.");
            return Collections.emptyList();
        }

        List<HechoDTO> acumulado = new ArrayList<>();

        for (FuenteMetamapa fuente : fuentes) {
            LocalDateTime fechaDesde = fuente.getUltimaConsulta(); // null si es la primera vez

            try {
                log.info("Consultando MetaMapa remoto '{}' en {} (desde={})",
                        fuente.getNombreFuente(), fuente.getBaseUrl(), fechaDesde);

                List<HechoDTO_IN> entrantes = adapter.obtenerHechos(
                        fuente.getBaseUrl(),
                        fechaDesde
                );

                List<HechoDTO> listos = entrantes.stream()
                        .map(in -> mapearHecho(in, fuente.getNombreFuente()))
                        .toList();

                acumulado.addAll(listos);

                fuente.setUltimaConsulta(LocalDateTime.now());
                fuente.setCantidadHechosUltima(listos.size());

            } catch (ExcepcionConexionMetamapa e) {
                log.error("Error al consultar la fuente '{}'", fuente.getNombreFuente(), e);

            }
        }

        fuenteRepo.saveAll(fuentes);

        return acumulado;
    }

    private HechoDTO mapearHecho(HechoDTO_IN in, String nombreFuente) {
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
                .origen(nombreFuente)
                .tipoFuente("PROXY")
                .build();
    }
}

