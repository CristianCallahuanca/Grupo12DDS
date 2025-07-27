package Fuentes.Proxy;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import Fuentes.FuenteEstatica.Dataset;
import Fuentes.FuenteEstatica.FuenteEstatica;
import Infraestructura.Repositorios.HechoRepositorio;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FuenteDemo extends FuenteProxy {
    //

    private static final FuenteDemo instance = new FuenteDemo();
    List<Hecho> hechos  = new ArrayList<>();

    //Singleton
    public FuenteDemo(){
    }

    public static FuenteDemo getInstancia() {
        return instance;
    }

    private AdaptadorDemo adapter;
    LocalDateTime ultimaActualizacion;

    public FuenteDemo(AdaptadorDemo adapter) {
        this.adapter = adapter;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }


    public void sincronizar() {
        List<Hecho> nuevosHechos = adapter.conseguirHechos();
        nuevosHechos.forEach(unHecho -> unHecho.setOrigen(Origen.PROXY));
        this.hechos.addAll(nuevosHechos);
        this.ultimaActualizacion = LocalDateTime.now();
    } // Esto lo aplicamos asi de esta forma pero en realidad luego tenemos que evaluar con los CRON JOBS
    //ya que este mecanismo no tiene que ser propio de esta clase
}

