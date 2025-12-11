package org.example.metamapa.service.implementacion;
import lombok.RequiredArgsConstructor;
import org.example.metamapa.exceptions.FuenteMetamapaDuplicadaException;
import org.example.metamapa.models.dtos.FuenteMetamapaDTO;
import org.example.metamapa.models.entidades.FuenteMetamapa;
import org.example.metamapa.models.repositorio.IFuenteMetamapaRepository;
import org.example.metamapa.service.IFuentesMetamapaService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FuentesMetamapaService implements IFuentesMetamapaService {

    private final IFuenteMetamapaRepository repo;

    @Override
    public FuenteMetamapaDTO registrarFuenteMetamapa(String nombreFuente, String baseUrl) {

        if (repo.existsByNombreFuente(nombreFuente)) {
            throw new FuenteMetamapaDuplicadaException(nombreFuente);
        }

        FuenteMetamapa entidad = new FuenteMetamapa();
        entidad.setNombreFuente(nombreFuente);
        entidad.setBaseUrl(baseUrl);
        entidad.setActiva(true);
        entidad.setUltimaConsulta(null);
        entidad.setCantidadHechosUltima(null);

        entidad = repo.save(entidad);

        return toDTO(entidad);
    }

    @Override
    public List<FuenteMetamapaDTO> listarFuentesMetamapa() {
        return repo.findByActivaTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private FuenteMetamapaDTO toDTO(FuenteMetamapa f) {
        FuenteMetamapaDTO dto = new FuenteMetamapaDTO();
        dto.setId(f.getId());
        dto.setNombreFuente(f.getNombreFuente());
        dto.setBaseUrl(f.getBaseUrl());
        return dto;
    }


    public void desactivarFuente(Long id) {
        FuenteMetamapa fuente = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fuente MetaMapa no encontrada con id=" + id));

        if (!fuente.isActiva()) {
            return;
        }

        fuente.setActiva(false);
        repo.save(fuente);
    }

}
