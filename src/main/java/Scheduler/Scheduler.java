package Scheduler;
import AdministracionDeHechos.Coleccion;
import Fuentes.Proxy.FuenteDemo;
import Fuentes.Proxy.FuenteProxy;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import Servicios.ServicioDeAgregacion;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.*;
import java.time.LocalDateTime;

public class Scheduler{

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public void iniciarScheduler() {

        ScheduleDemoAgregador();
        ScheduleAlgoritmosConsenso();
    }

    private void ScheduleDemoAgregador(){

        scheduler.scheduleAtFixedRate(() -> {

            FuenteDemo.getInstancia().sincronizar();
            try {
                ServicioDeAgregacion.getInstancia().actualizar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }, 0, 1, TimeUnit.HOURS);
    }

    private void ScheduleAlgoritmosConsenso(){

        int delay = calcularDelayInicial(3);
        int intervalo = 86400;

        scheduler.scheduleAtFixedRate(() -> {
            ColeccionRepositoryEnMemoria.getInstancia().getColecciones().forEach(Coleccion::consensuarHechos);

        },delay, intervalo, TimeUnit.SECONDS);
    }

    private int calcularDelayInicial(int delayHoras){ //te devuelve cuanto falta para las 3am en segundos

        LocalDateTime ahora = LocalDateTime.now();

        if(LocalTime.now().isBefore(LocalTime.of(delayHoras, 0))){
            //Es antes de las 3:00 AM
            return delayHoras * 60 * 60 - (ahora.getHour() * 60 * 60 + ahora.getMinute() * 60 + ahora.getSecond());
        }
        else{
            return 86400 - (ahora.getHour() * 60 * 60 + ahora.getMinute() * 60 + ahora.getSecond()) - delayHoras * 60 * 60;
        }
    }
}
