package org.example.metamapa.service;

import org.example.metamapa.models.dtos.FuenteMetamapaDTO;

import java.util.List;

public interface IFuentesMetamapaService {

    FuenteMetamapaDTO registrarFuenteMetamapa(String nombreFuente, String baseUrl);

    List<FuenteMetamapaDTO> listarFuentesMetamapa();

    void desactivarFuente(Long id);
}

