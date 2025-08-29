package org.example.metamapa.models.entidades;

import org.example.metamapa.service.adapters.implementaciones.AdapterFuenteDemo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FuenteDemo extends FuenteProxy {
    //
    private static final FuenteDemo instance = new FuenteDemo();
    List<HechoCrudo> hechos  = new ArrayList<>();

    //Singleton
    public FuenteDemo(){
    }

    public static FuenteDemo getInstancia() {
        return instance;
    }

    private AdapterFuenteDemo adapter;
    LocalDateTime ultimaActualizacion;

    public FuenteDemo(AdapterFuenteDemo adapter) {
        this.adapter = adapter;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }


    public void sincronizar() {
        List<HechoCrudo> nuevosHechos = adapter.conseguirHechos();
        this.hechos.addAll(nuevosHechos);
        this.ultimaActualizacion = LocalDateTime.now();
    } // Esto lo aplicamos asi de esta forma pero en realidad luego tenemos que evaluar con los CRON JOBS
    //ya que este mecanismo no tiene que ser propio de esta clase

}
