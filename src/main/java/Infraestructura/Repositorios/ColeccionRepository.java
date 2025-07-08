package Infraestructura.Repositorios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.Hecho;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ColeccionRepository {
    void guardar(Coleccion coleccion);
    public ArrayList<Coleccion> obtenerTodas();
    void eliminarPorHandle(String titulo);
}

