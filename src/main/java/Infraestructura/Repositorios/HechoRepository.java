package Infraestructura.Repositorios;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface HechoRepository {
    void guardar(Hecho hecho);
    Hecho buscarPorTitulo(String titulo);
    ArrayList<Hecho> obtenerTodas();
    void eliminarPorTitulo(String titulo);
    public List<Hecho> filtrarHechosDelSistema(List<CriterioDePertenencia> criterios) throws IOException;
}
