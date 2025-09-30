package org.example.metamapa.estadisticas.Servicios.implementaciones;

import org.example.metamapa.estadisticas.Models.repositorios.IColeccionesRepository;
import org.example.metamapa.estadisticas.Models.repositorios.IEstadisticasRepository;
import org.example.metamapa.estadisticas.Servicios.IEstadisticaService;
import org.springframework.stereotype.Service;

@Service
public class EstadisticaService implements IEstadisticaService {

    private IColeccionesRepository coleccionesRepository;
    private IEstadisticasRepository estadisticasRepository;
    EstadisticaService(IColeccionesRepository coleccionesRepository, IEstadisticasRepository estadisticasRepository){
        this.coleccionesRepository = coleccionesRepository;
        this.estadisticasRepository = estadisticasRepository;
    }
}
