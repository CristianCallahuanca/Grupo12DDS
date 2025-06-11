package AdministracionDeHechos;
import Fuentes.Fuente;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import Infraestructura.Repositorios.ColeccionRepositoryEnMemoria;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Getter
@Setter
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
        this.cargarColeccion();
    }

    public void cargarColeccion() {
        ColeccionRepositoryEnMemoria.getInstancia().guardar(this);
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
        return hechos.stream().filter(hecho->hecho.getIsVisible()).toList();
    }

    // Navegacion de Hechos
    public void navegarHechos(){
        this.imprimirHechos(this.obtenerHechos());
    }

    public void navegarHechosFiltrandoPor(List<CriterioDePertenencia> filtros){
        List <Hecho> hechosFiltrados = this.filtrarHechos(filtros);
        this.imprimirHechos(hechosFiltrados);
    }

    public void imprimirHechos(List<Hecho> unosHechos) {
        unosHechos.forEach(unHecho -> unHecho.imprimirHecho());
    }

    // Filtrar hechos
    public List<Hecho> filtrarHechos(List<CriterioDePertenencia> filtros){
        return this.obtenerHechos().stream().filter(unHecho -> this.filtarHecho(unHecho, filtros))
                .toList();
    }

    private boolean filtarHecho(Hecho unHecho,List<CriterioDePertenencia> filtros) {
        List<Boolean> CumplioCondiciones = filtros.stream()
                .map(unFiltro ->  cumpleElTipoDeFiltro(unHecho, unFiltro, filtros))
                .toList();
        //Dado un hecho y las condiciones, mapea cada condición, si la cumple queda true y sino false. Ej: CumplioCondiciones = [T,T,F,T]

        boolean todosTrue = CumplioCondiciones.stream().allMatch(Boolean::booleanValue); //Checkea que la lista este llena de true
        return todosTrue;
    }

    private boolean cumpleElTipoDeFiltro(Hecho unHecho, CriterioDePertenencia unFiltro, List<CriterioDePertenencia> filtros) {
        return filtros.stream()
                .filter( otroFiltro -> this.coincidenTipos(otroFiltro, unFiltro)) //esto nos deja los criterios que tenga el mismo tipo que un criterio
                .anyMatch(criterioFiltrado -> criterioFiltrado.cumpleUno(unHecho));
        // En la lista de criterios del mismo tipo, evalúo cada uno con unHecho, si uno solo cumple -> Devuelve true.
    }

    private Boolean coincidenTipos(CriterioDePertenencia unFiltro, CriterioDePertenencia otroFiltro) {
        return unFiltro.getClass() == otroFiltro.getClass();
    }

}

