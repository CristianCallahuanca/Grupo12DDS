package Fuentes.Proxy;

import AdministracionDeHechos.Hecho;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FuenteDemo extends FuenteProxy {
    private AdaptadorDemo adapter;
    LocalDateTime ultimaActualizacion;

    public FuenteDemo(AdaptadorDemo adapter) {
        this.adapter = adapter;
    }

    @Override
    public void sincronizar() {

        if (Duration.between(ultimaActualizacion, LocalDateTime.now()).toMinutes() >= 60) {
            List<Hecho> nuevosHechos = adapter.obtenerHechos();
            this.hechos.addAll(nuevosHechos);
            this.ultimaActualizacion = LocalDateTime.now();
        }
    } // Esto lo aplicamos asi de esta forma pero en realidad luego tenemos que evaluar con los CRON JOBS
    //ya que este mecanismo no tiene que ser propio de esta clase
}

