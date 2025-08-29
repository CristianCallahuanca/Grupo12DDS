package org.example.metamapa.models.entidades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FuenteProxy {
    private List<HechoCrudo> hechos = new ArrayList<>();

    public List<HechoCrudo> retornarHechos(){
        return hechos;
    }
    public void agregarHecho(HechoCrudo hecho){
        hechos.add(hecho);
    }

}
