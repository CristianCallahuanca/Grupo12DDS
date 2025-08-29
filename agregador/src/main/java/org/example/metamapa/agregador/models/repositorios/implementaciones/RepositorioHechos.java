package org.example.metamapa.agregador.models.repositorios.implementaciones;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioHechos implements IRepositorioHechos {

    private List<Hecho> hechos;

    private static final RepositorioHechos instancia = new RepositorioHechos();

    private RepositorioHechos() {}

    public static RepositorioHechos getInstance() {
        return instancia;
    }

    public List<Hecho> obtenerTodosLosHechosDelSistema(){
        return hechos;
    }

    public void guardarHecho(Hecho unHecho){
        hechos.add(unHecho);
    }

    public void guardarListaHechos(List<Hecho> hechos2){
        hechos.addAll(hechos2);
    }
}
