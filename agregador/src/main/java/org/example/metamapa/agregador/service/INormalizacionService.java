package org.example.metamapa.agregador.service;

import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface INormalizacionService {
    List<Hecho> normalizarHechos(List<HechoDTO_IN> hechosSinNormalizar);
}
