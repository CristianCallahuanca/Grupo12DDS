package org.example.metamapa.service;

import org.example.metamapa.models.dtos.HechoDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICargaMetamapaService {
    public ResponseEntity<List<HechoDTO>> obtenerHechos();
}
