package AdministracionDeHechos;
import AdministracionDeHechos.Consenso.AlgoritmoDeConsenso;
import AdministracionDeHechos.ModoNavegacion.ModoNavegacion;
import Fuentes.Fuente;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import Servicios.ServicioDeAgregacion;
import Servicios.ServicioDeIdentificacion;
import Servicios.ServicioFiltradorDeHechos;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.util.thread.Scheduler;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
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
        this.handle = ServicioDeIdentificacion.getInstancia().generarHandle();
        ServicioDeAgregacion.getInstancia().primeraCarga(this);
    }

    public void insertarHechos(List <Hecho> unosHechos) {this.hechos.addAll(unosHechos);}

    public void consensuarHechos(){
        this.algoritmoDeConsenso.verificar(this.hechos);
    }
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

