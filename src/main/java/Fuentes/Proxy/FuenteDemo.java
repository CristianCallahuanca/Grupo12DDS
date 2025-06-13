package Fuentes.Proxy;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FuenteDemo extends FuenteProxy {
    private AdaptadorDemo adapter;
    LocalDateTime ultimaActualizacion;

    public FuenteDemo(AdaptadorDemo adapter) {
        this.adapter = adapter;
        this.ultimaActualizacion = LocalDateTime.now();
    }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    @Override
    public void sincronizar() {

        if (Duration.between(ultimaActualizacion, LocalDateTime.now()).toMinutes() >= 60) {
            List<Hecho> nuevosHechos = adapter.obtenerHechos();
            nuevosHechos.forEach(unHecho -> unHecho.setOrigen(Origen.PROXY));
            this.hechos.addAll(nuevosHechos);
            this.ultimaActualizacion = LocalDateTime.now();
        }
    } // Esto lo aplicamos asi de esta forma pero en realidad luego tenemos que evaluar con los CRON JOBS
    //ya que este mecanismo no tiene que ser propio de esta clase

}

