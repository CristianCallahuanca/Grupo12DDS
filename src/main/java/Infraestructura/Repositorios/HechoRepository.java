package Infraestructura.Repositorios;

import AdministracionDeHechos.Hecho;

import java.util.ArrayList;
public interface HechoRepository {
    void guardar(Hecho hecho);
    Hecho buscarPorTitulo(String titulo);
    ArrayList<Hecho> obtenerTodas();
    void eliminarPorTitulo(String titulo);
}
