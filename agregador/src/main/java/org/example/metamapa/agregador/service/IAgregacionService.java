package org.example.metamapa.agregador.service;

import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAgregacionService {
    List<HechoDTO_IN> getHechosDTO3FuentesSinLimpiar();
    void integrarHechosFuentes();
}
