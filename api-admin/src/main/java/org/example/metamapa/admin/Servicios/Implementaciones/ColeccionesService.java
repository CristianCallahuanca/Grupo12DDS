package org.example.metamapa.admin.Servicios.Implementaciones;

import org.example.metamapa.admin.Modelos.Repositorios.IColeccionRepository;
import org.example.metamapa.admin.Modelos.Repositorios.implementaciones.ColeccionRepository;
import org.example.metamapa.admin.Servicios.IColeccionesService;
import org.springframework.stereotype.Service;

@Service
public class ColeccionesService implements IColeccionesService {

    private final IColeccionRepository coleccionRepository;

    public ColeccionesService(IColeccionRepository coleccionRepository){
        this.coleccionRepository = coleccionRepository;
    }

    public String obtenerSaludo(){
        return "buenas";
    }

}
