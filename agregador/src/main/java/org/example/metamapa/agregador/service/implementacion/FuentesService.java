package org.example.metamapa.agregador.service.implementacion;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.models.dtos.DTO_IN.FuenteDTO;
import org.example.metamapa.agregador.models.entidades.Fuente;
import org.example.metamapa.agregador.models.repositorios.IFuenteRepository;
import org.example.metamapa.agregador.service.IFuentesService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FuentesService implements IFuentesService {

    private final IFuenteRepository fuenteRepository;

    public FuentesService(IFuenteRepository fuenteRepository) {
        this.fuenteRepository = fuenteRepository;
    }

    @Override
    public void registrarFuente(FuenteDTO dto) {
        Fuente fuente = new Fuente(dto.getNombreFuente(), dto.getTipo(), dto.getBaseUrl());
        fuenteRepository.save(fuente);
        log.info("Fuente registrada o confirmada: {}", dto.getNombreFuente());
    }
}

