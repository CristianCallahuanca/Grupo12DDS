package Infraestructura.Repositorios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.Hecho;

import java.util.ArrayList;
import java.util.List;

public class ColeccionRepositoryEnMemoria implements ColeccionRepository{
    private static final ColeccionRepositoryEnMemoria instance = new ColeccionRepositoryEnMemoria();
    private List<Coleccion> colecciones = new ArrayList<>();

    private ColeccionRepositoryEnMemoria() {
    }

    public static ColeccionRepositoryEnMemoria getInstancia() {
        return instance;
    }

    @Override
    public void guardar(Coleccion coleccion) {
        colecciones.removeIf(c -> c.getHandle().equals(coleccion.getHandle()));
        colecciones.add(coleccion);
    }


    @Override
    public Coleccion buscarPorTitulo(String titulo) {
        for (Coleccion c : colecciones) {
            if (c.getTitulo().equals(titulo)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Coleccion buscarPorHandle(String handle) {
        for (Coleccion c : colecciones) {
            if (c.getHandle().equals(handle)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Coleccion> obtenerTodas() {
        return new ArrayList<>(this.colecciones);
    }

    @Override
    public void eliminarPorTitulo(String titulo) {
        colecciones.removeIf(c -> c.getTitulo().equals(titulo));
    }

}
