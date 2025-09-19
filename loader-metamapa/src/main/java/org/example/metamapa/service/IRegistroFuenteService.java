package org.example.metamapa.service;

import org.example.metamapa.models.entidades.FuenteConfigurada;

import java.util.List;
import java.util.Optional;

public interface IRegistroFuenteService {

    void registrarFuente(FuenteConfigurada fuente);

    List<FuenteConfigurada> obtenerFuentes();

    Optional<FuenteConfigurada> buscarPorNombre(String nombre);

    void eliminarFuente(String nombre);
}
