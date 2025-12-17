package org.example.metamapa.Servicios.implementaciones;

import org.example.metamapa.Servicios.IDireccionesIpService;
import org.example.metamapa.models.entidades.DireccionesIP;
import org.example.metamapa.models.repos.IDireccionesIPRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionesIpService implements IDireccionesIpService {

    private final IDireccionesIPRepository repository;

    public DireccionesIpService(IDireccionesIPRepository repository) {
        this.repository = repository;
    }

    @Override
    public DireccionesIP guardar(DireccionesIP direccionIP) {
        return repository.save(direccionIP);
    }

    @Override
    public void eliminarPorId(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<DireccionesIP> obtenerTodas() {
        return repository.findAll();
    }

    @Override
    public boolean ipBloqueada(String ip) {
        return repository.existsByDireccionIp(ip);
    }
}
