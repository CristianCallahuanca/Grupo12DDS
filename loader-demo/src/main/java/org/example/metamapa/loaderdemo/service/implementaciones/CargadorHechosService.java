package org.example.metamapa.loaderdemo.service.implementaciones;

import lombok.RequiredArgsConstructor;
import org.example.metamapa.loaderdemo.infraestructura.adapters.IAdapterFuenteDemo;
import org.example.metamapa.loaderdemo.models.repositorio.IRepositorioHechos;
import org.example.metamapa.loaderdemo.service.ICargadorHechosService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargadorHechosService implements ICargadorHechosService {

    private final IAdapterFuenteDemo adapter;
    private final IRepositorioHechos repositorio;

    @Override
    public void cargarSiguienteHecho() {
        adapter.obtenerSiguienteHecho().ifPresent(repositorio::save);
    }
}
