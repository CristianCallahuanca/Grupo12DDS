package org.example.metamapa.agregador.models.repositorios;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.repositorios.implementaciones.RepositorioHechos;

import java.util.List;

public interface IRepositorioHechos {
    List<Hecho> obtenerTodosLosHechosDelSistema();
    void guardarHecho(Hecho unHecho);
}
