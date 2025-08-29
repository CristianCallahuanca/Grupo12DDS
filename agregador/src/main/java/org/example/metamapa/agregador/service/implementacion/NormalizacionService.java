package org.example.metamapa.agregador.service.implementacion;

import org.example.metamapa.agregador.models.dtos.HechoDTO;
import java.util.List;

public class NormalizacionService {

    /*
    private List<Hecho> obtenerHechosDeTerceros(){
        List<Hecho> var = RepositorioHechos.getInstance().obtenerTodosLosHechosDelSistema();
        
        return var.stream().filter(h -> h.getOrigen() == Origen.PROXY ||h.getOrigen() == Origen.ESTATICA).toList();
    }*/

    public List<HechoDTO> normalizarUbicacion(List<HechoDTO> hechosCrudos){

    }

}
