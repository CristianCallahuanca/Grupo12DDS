package Infraestructura.Repositorios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ColeccionRepositorio {

    private static final ColeccionRepositorio instance = new ColeccionRepositorio();
    private List<Coleccion> colecciones = new ArrayList<>();

    private ColeccionRepositorio() {
    }

    public static ColeccionRepositorio getInstancia() {
        return instance;
    }

    public void guardar(Coleccion coleccion) {
        colecciones.removeIf(c -> c.getHandle().equals(coleccion.getHandle()));
        colecciones.add(coleccion);
    }

    public ArrayList<Coleccion> obtenerTodas() {
        return new ArrayList<>(this.colecciones);
    }

    public void eliminarPorHandle(String handle) {
        colecciones.removeIf(c-> c.getHandle().equals(handle));
    }

}

