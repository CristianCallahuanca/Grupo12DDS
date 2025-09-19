package org.example.metamapa.models.repositorios;

import org.example.metamapa.models.entidades.FuenteConfigurada;

import java.util.List;
import java.util.Optional;

public interface IRepositorioFuenteProxy {

    void guardar(FuenteConfigurada fuente);

    List<FuenteConfigurada> obtenerTodas();

    Optional<FuenteConfigurada> buscarPorNombre(String nombre);

    void eliminar(String nombre);
}
