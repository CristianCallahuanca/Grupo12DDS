package Infraestructura.Repositorios;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Fuentes.FuenteDinamica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface FuenteDinamicaRepository {
    void guardar(Hecho hecho);
    Hecho buscarPorTitulo(String titulo);
    public ArrayList<Hecho> obtenerTodas();
    void eliminarPorTitulo(String titulo);
}
