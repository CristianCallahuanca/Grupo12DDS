package Infraestructura.Repositorios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
    public ArrayList<Coleccion> obtenerTodas() {
        return new ArrayList<>(this.colecciones);
    }

    @Override
    public void eliminarPorHandle(String handle) {
        colecciones.removeIf(c-> c.getHandle().equals(handle));
    }

}
