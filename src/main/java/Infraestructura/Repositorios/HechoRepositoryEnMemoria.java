package Infraestructura.Repositorios;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HechoRepositoryEnMemoria implements HechoRepository{

    private static final HechoRepositoryEnMemoria instance = new HechoRepositoryEnMemoria();
    private List<Hecho> hechos = new ArrayList<>();

    private HechoRepositoryEnMemoria() {
    }

    public static HechoRepositoryEnMemoria getInstancia() {
        return instance;
    }

    @Override
    public void guardar(Hecho hecho) {
        hechos.add(hecho);
    }

    @Override
    public List<Hecho> filtrarHechosDelSistema(List<CriterioDePertenencia> criterios) throws IOException {
        return hechos.stream().filter(unHecho -> unHecho.filtarHecho(criterios))
                     .toList();
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

}


