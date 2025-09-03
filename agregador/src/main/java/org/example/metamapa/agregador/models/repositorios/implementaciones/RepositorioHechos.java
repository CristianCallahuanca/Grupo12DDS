package org.example.metamapa.agregador.models.repositorios.implementaciones;

import dinamico.models.entidades.hecho.HechoCrudo;
import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioHechos implements IRepositorioHechos {
    private final List<Hecho> hechos = new ArrayList<>();

    @Override
    public List<Hecho> obtenerTodosLosHechosDelSistema(){
        return hechos;
    }

    @Override
    public void guardarHecho(Hecho unHecho){
        hechos.add(unHecho);
    }

    @Override
    public void guardarListaHechos(List<Hecho> hechos2){
        hechos.addAll(hechos2);
    }
}
