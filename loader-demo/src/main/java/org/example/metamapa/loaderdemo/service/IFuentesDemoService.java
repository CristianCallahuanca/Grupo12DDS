package org.example.metamapa.loaderdemo.service;

import org.example.metamapa.loaderdemo.models.dto.FuenteDemoDTO;

import java.util.List;

public interface IFuentesDemoService {

    FuenteDemoDTO registrarFuenteDemo(String nombreFuente, String url);

    List<FuenteDemoDTO> listarFuentesDemo();
}
