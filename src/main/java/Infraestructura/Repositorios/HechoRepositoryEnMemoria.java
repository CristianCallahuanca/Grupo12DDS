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
    public List<Hecho> filtrarHechosDelSistema(List<CriterioDePertenencia> criterios) throws IOException {
        List<Hecho> hechosDelSistema = new ArrayList<>(hechos.stream().filter(unHecho -> unHecho.filtrarHecho(criterios))
                .toList());
        hechosDelSistema.addAll(FuenteEstatica.getInstancia().filtrarHechos(criterios));

        return hechosDelSistema;
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

    @Override
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


