package Servicios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorFuente;
import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
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

    public void primeraCarga(List <Fuente> fuentes,
                             List <CriterioDePertenencia> criterios,
                             Coleccion unaColeccion) throws IOException {

        List <Hecho> hechosDelSistema = HechoRepositoryEnMemoria.getInstancia().obtenerTodosLosHechosDelSistema();

        List <CriterioDePertenencia> fuentesABuscar = new ArrayList<>();
        fuentes.forEach(unaFuente -> fuentesABuscar.add(new PorFuente(unaFuente)));

        List <Hecho> hechosDeLaColeccion = ServicioFiltradorDeHechos.filtrarHechos(hechosDelSistema, fuentesABuscar);

        unaColeccion.insertarHechos(hechosDeLaColeccion);

    }

}
