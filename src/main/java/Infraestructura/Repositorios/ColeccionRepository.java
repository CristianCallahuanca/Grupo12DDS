package Infraestructura.Repositorios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.Hecho;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ColeccionRepository {
    void guardar(Coleccion coleccion);
    Coleccion buscarPorTitulo(String titulo);
    public ArrayList<Coleccion> obtenerTodas();
    void eliminarPorTitulo(String titulo);
}

