package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IContribuyenteService;
import org.example.metamapa.gestordatos.models.dtos.input.ContribuyenteRegInputDTO;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.example.metamapa.gestordatos.models.repositorios.IContribuyenteRepository;
import org.springframework.stereotype.Service;

@Service
public class ContribuyenteService implements IContribuyenteService {

    private final IContribuyenteRepository contribuyenteRepository;

    ContribuyenteService(IContribuyenteRepository contribuyenteRepository) {
        this.contribuyenteRepository = contribuyenteRepository;
    }

    public ContribuyenteRegistrado crearContribuyenteRegistrado(ContribuyenteRegInputDTO constribuyenteInputDTO) {

        ContribuyenteRegistrado contribuyente = new ContribuyenteRegistrado(constribuyenteInputDTO.getNombre(),
                constribuyenteInputDTO.getApellido(), constribuyenteInputDTO.getEdad(), constribuyenteInputDTO.getDni());

        contribuyenteRepository.save(contribuyente);

        return contribuyente;
    }
}
