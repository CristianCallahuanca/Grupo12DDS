package org.example.metamapa.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.adapters.IAdapterMetamapa;
import org.example.metamapa.models.dtos.HechoDTO;
import org.example.metamapa.models.dtos.HechoDTO_IN;
import org.example.metamapa.models.entidades.EstadoConsulta;
import org.example.metamapa.models.repositorio.IEstadoConsultaRepositorio;
import org.example.metamapa.service.ICargaMetamapaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CargaMetamapaService implements ICargaMetamapaService {

    private final IAdapterMetamapa adapter;
    private final IEstadoConsultaRepositorio estadoRepo;

    @Value("${loader.id}")
    private String loaderId;

    @Override
    public List<HechoDTO> obtenerHechos() {
        LocalDateTime ultimaConsulta = estadoRepo.findById(loaderId)
                .map(EstadoConsulta::getUltimaConsulta)
                .orElse(null);

        List<HechoDTO_IN> hechosEntrantes = adapter.obtenerHechos(ultimaConsulta);

        List<HechoDTO> hechosListos = hechosEntrantes.stream()
                .map(this::mapearHecho)
                .toList();

        registrarEstado(hechosListos);

        return hechosListos;
    }

    private void registrarEstado(List<HechoDTO> hechosListos) {
        EstadoConsulta estado = new EstadoConsulta(
                loaderId,
                LocalDateTime.now(),
                hechosListos.size(),
                "OK"
        );
        estadoRepo.save(estado);
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

