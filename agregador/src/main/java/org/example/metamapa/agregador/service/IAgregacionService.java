package org.example.metamapa.agregador.service;

import org.example.metamapa.agregador.models.dtos.DTO_IN.HechoDTO_IN;
import org.springframework.stereotype.Service;


@Service
public interface IAgregacionService {

    void integrarHechosFuentes();
}
