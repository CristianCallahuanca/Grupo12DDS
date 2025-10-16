package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IHechoColeccionService;
import org.example.metamapa.gestordatos.models.repositorios.IHechosColeccionRepository;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HechoColeccionService implements IHechoColeccionService {

    private final IHechosColeccionRepository hechosColeccionRepository;

    public HechoColeccionService(IHechosColeccionRepository hechosColeccionRepository) {
        this. hechosColeccionRepository =  hechosColeccionRepository;
    }

    public List<Long> obtenerIdsHechosAsociadosColeccion(String handle){
        List<Long> idHechos = new ArrayList<>();//= hechosColeccionRepository.findIdsHechosByColeccionHandle(handle);

        return idHechos;
    }

    public void actualizarHechosDeColeccion(List<HechoDeColeccion> hechos) {
        if (hechos != null && !hechos.isEmpty()) {
            hechosColeccionRepository.saveAll(hechos);
        }
    }
}
