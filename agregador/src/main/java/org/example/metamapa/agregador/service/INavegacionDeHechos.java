package org.example.metamapa.agregador.service;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.filtros.FilterCondition;

import java.util.List;

public interface INavegacionDeHechos {
    List<Hecho> filtrarHechos(List<Hecho> unosHechos, List<FilterCondition> filtros);
    boolean filtrarHecho(Hecho unHecho,List<FilterCondition> filtros);
    boolean cumpleElTipoDeFiltro(Hecho unHecho, FilterCondition unFiltro, List<FilterCondition> filtros);
    boolean coincidenTipos(FilterCondition unFiltro, FilterCondition otroFiltro);
}
