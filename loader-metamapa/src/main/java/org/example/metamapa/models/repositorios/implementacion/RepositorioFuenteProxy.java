package org.example.metamapa.models.repositorios.implementacion;

import org.example.metamapa.models.entidades.FuenteConfigurada;
import org.example.metamapa.models.repositorios.IRepositorioFuenteProxy;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RepositorioFuenteProxy implements IRepositorioFuenteProxy {

    private final Map<String, FuenteConfigurada> fuentes = new HashMap<>();

    @Override
    public void guardar(FuenteConfigurada fuente) {
        fuentes.put(fuente.getNombre(), fuente);
    }

    @Override
    public List<FuenteConfigurada> obtenerTodas() {
        return new ArrayList<>(fuentes.values());
    }

    @Override
    public Optional<FuenteConfigurada> buscarPorNombre(String nombre) {
        return Optional.ofNullable(fuentes.get(nombre));
    }

    @Override
    public void eliminar(String nombre) {
        fuentes.remove(nombre);
    }
}
