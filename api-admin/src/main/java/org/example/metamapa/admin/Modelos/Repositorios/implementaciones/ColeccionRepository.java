package org.example.metamapa.admin.Modelos.Repositorios.implementaciones;

import org.example.metamapa.admin.Modelos.Repositorios.IColeccionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ColeccionRepository implements IColeccionRepository {

    private final List<String> colecciones = new ArrayList<>();

    public ColeccionRepository() {
        colecciones.add("coleccion 1");
        colecciones.add("Coleccion 2");
    }

    public List<String> obtenerColecciones() {
        return colecciones;
    }
}
