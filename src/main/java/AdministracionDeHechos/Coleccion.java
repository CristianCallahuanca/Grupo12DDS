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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void cargarHechos(Fuente fuente, List<CriterioDePertenencia> criterios){

        this.hechos = fuente.filtrarHechos(criterios);
    }

    public List<Hecho> obtenerHechos() {
        return hechos.stream().filter(Hecho::isVisible).toList();
    }

}

