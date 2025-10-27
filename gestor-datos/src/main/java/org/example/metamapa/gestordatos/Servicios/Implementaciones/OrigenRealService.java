package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.models.dtos.output.OrigenRealDTO;
import org.example.metamapa.gestordatos.models.entidades.OrigenReal;
import org.example.metamapa.gestordatos.models.repositorios.IOrigenRealRepository;
import org.example.metamapa.gestordatos.Servicios.IOrigenRealService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrigenRealService implements IOrigenRealService {

    private final IOrigenRealRepository origenRealRepository;

    @Override
    public List<OrigenRealDTO> listarTodos() {
        return origenRealRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private OrigenRealDTO toDTO(OrigenReal origen) {
        OrigenRealDTO dto = new OrigenRealDTO();
        dto.setNombre(origen.getNombre());
        dto.setTipoFuente(origen.getTipoFuente().name());
        return dto;
    }

}
