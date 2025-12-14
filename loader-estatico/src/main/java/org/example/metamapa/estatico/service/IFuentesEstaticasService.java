package org.example.metamapa.estatico.service;

import org.example.metamapa.estatico.models.dtos.FuenteEstaticaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFuentesEstaticasService {

    FuenteEstaticaDTO registrarFuenteDesdeCsv(String nombreFuente, MultipartFile archivoCsv) throws IOException;

    //FuenteEstaticaDTO actualizarFuenteCsv(Long fuenteId, MultipartFile archivoCsv);

    List<FuenteEstaticaDTO> listarFuentes();
}

