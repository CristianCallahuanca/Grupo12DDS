package AdministracionDeHechos;
import AdministracionDeHechos.Consenso.AlgoritmoDeConsenso;
import AdministracionDeHechos.ModoNavegacion.ModoNavegacion;
import Fuentes.Fuente;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import Servicios.ServicioDeAgregacion;
import Servicios.ServicioFiltradorDeHechos;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.util.thread.Scheduler;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Coleccion {
    private List<Fuente> fuentes;
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenencia> criterios;
    private List<Hecho> hechos;
    private String handle;
    private AlgoritmoDeConsenso algoritmoDeConsenso;


    //Si List<CriterioDePertenencia> criterios es null puede romper
    public Coleccion(List<Fuente> fuentes, String titulo, String descripcion, List<CriterioDePertenencia> criterios) throws IOException {
        this.fuentes = fuentes;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios = criterios;
        this.handle = generarHandle(titulo);
        ServicioDeAgregacion.getInstancia().primeraCarga(this.fuentes, this.criterios, this);
    }

    private String generarHandle(String titulo) {
        // Quita acentos
        String normalizado = Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // Elimina caracteres no alfanuméricos excepto espacios, y reemplaza espacios por guiones
        return normalizado.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", "-");
    }

    public void insertarHechos(List <Hecho> unosHechos) {this.hechos.addAll(unosHechos);}
/*
    private void consensuarHechos(){
        // 1. Crear el JobDetail con la clase MiJob
        JobDetail job = JobBuilder.newJob(MiJob.class)
                .withIdentity("miTarea", "grupo1")
                .build();

        // 2. Crear el trigger que dispare todos los días a las 15:30
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("miTrigger", "grupo1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(15, 30))
                .build();

        // 3. Obtener el scheduler
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        // 4. Iniciar el scheduler
        scheduler.start();

        // 5. Agendar la tarea
        scheduler.scheduleJob(job, trigger);
    }
*/

    public List <Hecho> obtenerHechosPorModo(ModoNavegacion algunModo)
    {
        return algunModo.aplicarModoDeNavegacion(this.hechos, this.algoritmoDeConsenso);
    }

    private void cargarHechosDeUnaFuente(Fuente fuente, List<CriterioDePertenencia> criterios) throws IOException {
        this.hechos.addAll(ServicioFiltradorDeHechos.filtrarHechos(fuente.obtenerHechos(),criterios));

    }

    public List<Hecho> obtenerHechos() {
        return hechos.stream().filter(hecho->hecho.getVisible()).toList();
    }


    //Solo para tests
    public void imprimirHechos(List<Hecho> unosHechos) {
        unosHechos.forEach(unHecho -> unHecho.imprimirHecho());
    }

    /*
    ===================================
    ==============DEPRECADO============
    ===================================


    private void cargarColeccion() {
        ColeccionRepositoryEnMemoria.getInstancia().guardar(this);
    }

    public void cargarHechos(List<Fuente> fuentes, List<CriterioDePertenencia> criterios) throws IOException {
        fuentes.forEach(unaFuente -> {
            try {
                this.cargarHechosDeUnaFuente(unaFuente, criterios);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    */
}

