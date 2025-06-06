package AdministracionDeHechos;
import Fuentes.Fuente;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import java.util.List;

public class Coleccion {
    private Fuente fuente;
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenencia> criterios;
    private List<Hecho> hechos;
    private String handle;

    public Coleccion(Fuente fuente, String titulo, String descripcion, List<CriterioDePertenencia> criterios, String handle) {
        this.fuente = fuente;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios = criterios;
        this.handle = handle;
        this.cargarHechos(fuente,criterios);
    }

    public void cargarHechos(Fuente fuente, List<CriterioDePertenencia> criterios){

        this.hechos = Fuente.filtrarHechos(criterios);
    }
}

