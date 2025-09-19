package org.example.metamapa.service;

import org.example.metamapa.models.dtos.HechoDTO;

import java.util.List;

public interface ICargaProxyService {
    List<HechoDTO> cargarHechos();
}
