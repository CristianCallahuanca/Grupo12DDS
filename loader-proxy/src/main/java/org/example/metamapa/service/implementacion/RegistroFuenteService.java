package org.example.metamapa.service.implementacion;

import org.example.metamapa.models.entidades.FuenteConfigurada;
import org.example.metamapa.models.repositorios.IRepositorioFuenteProxy;
import org.example.metamapa.service.IRegistroFuenteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroFuenteService implements IRegistroFuenteService {

    private final IRepositorioFuenteProxy repositorio;

    public RegistroFuenteService(IRepositorioFuenteProxy repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarFuente(FuenteConfigurada fuente) {
        if (repositorio.buscarPorNombre(fuente.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una fuente con el nombre: " + fuente.getNombre());
        }
        repositorio.guardar(fuente);
    }

    @Override
    public List<FuenteConfigurada> obtenerFuentes() {
        return repositorio.obtenerTodas();
    }

    @Override
    public Optional<FuenteConfigurada> buscarPorNombre(String nombre) {
        return repositorio.buscarPorNombre(nombre);
    }

    @Override
    public void eliminarFuente(String nombre) {
        if (repositorio.buscarPorNombre(nombre).isEmpty()) {
            throw new IllegalArgumentException("No existe una fuente con el nombre: " + nombre);
        }
        repositorio.eliminar(nombre);
    }
}
