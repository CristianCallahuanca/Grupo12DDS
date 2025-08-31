package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.models.entidades.Hecho;
import org.example.metamapa.agregador.models.entidades.filtros.FilterCondition;
import org.example.metamapa.agregador.models.entidades.filtros.PorFechaCarga;
import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DuplicacionService {
    /*
    * Si un varios hechos ocurrieron en el mismo lugar  && fecha de acontecimiento son candidatos a ser duplicado
    *   si además tienen el mismo (titulo && categoria) || id_contribuyente podemos asegurarlo
    * */

    @Autowired
    private IRepositorioHechos  repositorioHechos;

    private List<Hecho> obtenerLosUltimosHechos(){
        LocalDateTime horaActual = LocalDateTime.now();
        FilterCondition ultimaHora = new PorFechaCarga(horaActual.minusHours(1), horaActual);

        return repositorioHechos.obtenerTodosLosHechosDelSistema().stream().
                filter(ultimaHora::cumpleUno).toList();
    }

    private List<Hecho> obtenerHechosMismoLugar(List<Hecho> hechos){
        

        return null;
    }
}







