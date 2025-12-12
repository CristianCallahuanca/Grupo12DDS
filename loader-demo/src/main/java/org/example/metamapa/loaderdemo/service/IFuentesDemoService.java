package org.example.metamapa.loaderdemo.service;

import org.example.metamapa.loaderdemo.models.dto.FuenteDemoDTO;
import org.example.metamapa.loaderdemo.models.entidades.FuenteDemo;

import java.util.List;

public interface IFuentesDemoService {

    FuenteDemoDTO registrarFuenteDemo(String nombreFuente, String url,String pathApi, String email,
                                      String password);

    List<FuenteDemoDTO> listarFuentesDemo();

    List<FuenteDemo> obtenerFuentesActivas();   // para el cargador

}
