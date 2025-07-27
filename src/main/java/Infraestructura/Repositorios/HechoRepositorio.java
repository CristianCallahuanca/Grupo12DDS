package Infraestructura.Repositorios;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Fuentes.FuenteEstatica.FuenteEstatica;

public class HechoRepositorio{

    private static HechoRepositorio instance = null;
    private List<Hecho> hechos = new ArrayList<>();

    public HechoRepositorio() {
    }

    public static HechoRepositorio getInstancia() {
        if (instance == null) {
            instance = new HechoRepositorio();
        }
        return instance;
    }

    public void guardar(Hecho hecho) {
        hechos.add(hecho);
    }

    //incluye estatica
    public ArrayList<Hecho> obtenerTodosLosHechosDelSistema() throws IOException {
        ArrayList<Hecho> todos = new ArrayList<>(hechos);  // Copiamos los hechos actuales
        todos.addAll(FuenteEstatica.getInstancia().obtenerHechos());  // Agregamos los de la fuente estática
        return todos;  // Devolvemos la lista combinada
    }

    //solo los que están en este repo: los de dinamica y proxy
    public ArrayList<Hecho> obtenerTodas() {
        return new ArrayList<>(this.hechos);
    }

    public void eliminarPorTitulo(String titulo) {
        hechos.removeIf(h -> h.getTitulo().equals(titulo));
    }
    public void limpiar() {
        this.hechos.clear();
    }
}


