package Infraestructura.Repositorios;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Fuentes.FuenteEstatica.FuenteEstatica;

public class HechoRepositoryEnMemoria implements HechoRepository{

    private static HechoRepositoryEnMemoria instance = null;
    private List<Hecho> hechos = new ArrayList<>();

    public HechoRepositoryEnMemoria() {
    }

    public static HechoRepositoryEnMemoria getInstancia() {
        if (instance == null) {
            instance = new HechoRepositoryEnMemoria();
        }
        return instance;
    }

    @Override
    public void guardar(Hecho hecho) {
        hechos.add(hecho);
    }
    

    @Override
    public Hecho buscarPorTitulo(String titulo) {
        for (Hecho h : hechos) {
            if (h.getTitulo().equals(titulo)) {
                return h;
            }
        }
        return null;
    }

    //incluye estatica
    public ArrayList<Hecho> obtenerTodosLosHechosDelSistema() throws IOException {
        ArrayList<Hecho> todos = new ArrayList<>(hechos);  // Copiamos los hechos actuales
        todos.addAll(FuenteEstatica.getInstancia().obtenerHechos());  // Agregamos los de la fuente estática
        return todos;  // Devolvemos la lista combinada
    }

    @Override //solo los que están en este repo: los de dinamica y proxy
    public ArrayList<Hecho> obtenerTodas() {
        return new ArrayList<>(this.hechos);
    }

    @Override
    public void eliminarPorTitulo(String titulo) {
        hechos.removeIf(h -> h.getTitulo().equals(titulo));
    }
    public void limpiar() {
        this.hechos.clear();
    }
}


