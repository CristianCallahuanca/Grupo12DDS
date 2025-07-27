package Servicios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorFuente;
import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.ColeccionRepositorio;
import Infraestructura.Repositorios.HechoRepositorio;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServicioDeAgregacion {

    private static final ServicioDeAgregacion instance = new ServicioDeAgregacion();
    private List<Fuente> fuentes = new ArrayList<>();

    private LocalDateTime ultimaEjecucion;

    private ServicioDeAgregacion() {}

    public static ServicioDeAgregacion getInstancia() {
        return instance;
    }

    public void guardar(Fuente fuente){fuentes.add(fuente);}

    public void primeraCarga(Coleccion unaColeccion) throws IOException {

        List <Hecho> hechosDelSistema = HechoRepositorio.getInstancia().obtenerTodosLosHechosDelSistema();

        this.agregarHechosEnColeccion(unaColeccion, hechosDelSistema);

    }

    public void actualizar() throws IOException {

        LocalDateTime ahora = LocalDateTime.now();

        List<Hecho> hechosNuevos = this.obtenerHechosNuevosDesde(ultimaEjecucion);

        for (Coleccion col : ColeccionRepositorio.getInstancia().obtenerTodas()){
        this.agregarHechosEnColeccion(col, hechosNuevos);
        }

        ultimaEjecucion = ahora;

    }

    public void agregarHechosEnColeccion(Coleccion unaColeccion, List<Hecho> hechos){
        List <CriterioDePertenencia> fuentesABuscar = new ArrayList<>();
        unaColeccion.getFuentes().forEach(unaFuente -> fuentesABuscar.add(new PorFuente(unaFuente)));

        List <Hecho> hechosDeCiertasFuentes = ServicioFiltradorDeHechos.filtrarHechos(hechos, fuentesABuscar);
        List <Hecho> hechosDeLaColeccion = ServicioFiltradorDeHechos.filtrarHechos(hechosDeCiertasFuentes, unaColeccion.getCriterios());

        unaColeccion.insertarHechos(hechosDeLaColeccion);
    }

    private List<Hecho> obtenerHechosNuevosDesde(LocalDateTime fecha) throws IOException {
         List <Hecho> hechosDelSistema = HechoRepositorio.getInstancia().obtenerTodosLosHechosDelSistema();

                return hechosDelSistema.stream().filter(h -> h.getFechaCarga().isAfter(fecha)).toList();
    }
}