package AdministracionDeHechos;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import Fuentes.FuenteDinamica;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;
import Persona.Contribuyente.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.Main.logger;

@Getter
@Setter
public class Hecho {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private boolean visible;

    public boolean getVisible() {
        return visible;
    }

    private Origen origen;
    private List<String> archivosMultimedia;
    private String etiqueta;
    private Contribuyente contribuyente;

    public Hecho(String titulo, String descripcion, String categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,String etiqueta) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.etiqueta = etiqueta;
        this.visible = true;
        this.fechaCarga = LocalDateTime.now();
    }

    public void setOrigen(Origen unOrigen) {
        this.origen = unOrigen;
        if (unOrigen != Origen.ESTATICA) {HechoRepositoryEnMemoria.getInstancia().guardar(this);}
    }

    //METODOS DE HECHOS
    public void marcarComoNoVisible() {
        this.visible = false;
    }

    public void editarCon(Hecho cambios) {
        if (this.puedeSerEditado()) {
            this.titulo = cambios.getTitulo();
            this.descripcion = cambios.getDescripcion();
            this.categoria = cambios.getCategoria();
            this.ubicacion = cambios.getUbicacion();
            this.etiqueta = cambios.getEtiqueta();
            this.archivosMultimedia = new ArrayList<>(cambios.getArchivosMultimedia());
            this.fechaAcontecimiento = cambios.getFechaAcontecimiento();
            //NO cambiar contribuyente, origen ni fecha de carga
        } else {
            throw new IllegalStateException("El hecho ya no puede ser editado.");
        }
    }

    public void imprimirHecho() {
        logger.info("Título: {}", this.getTitulo());
        logger.info("Descripción: {}", this.getDescripcion());
        logger.info("Categoría: {}", this.getCategoria());
        logger.info("Ubicación: {}", this.getUbicacion());
        logger.info("Fecha del hecho: {}", this.getFechaAcontecimiento());
        logger.info("Fecha de carga: {}", this.getFechaAcontecimiento());
        logger.info("Origen: {}", this.getOrigen());
        logger.info("-------------------------------------------");

    }

    public boolean puedeSerEditado() {
        return this.origen == Origen.DINAMICA && //hay que ver que sea registrado
                ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) <= 7;
        // Con esto basta para saber si puede ser editado?
    }

    //Se fija si un hecho cumple una lista de criterios y retorna BOOL. NO FILTRA
    public boolean filtrarHecho(List<CriterioDePertenencia> filtros) {
        // Para cada tipo de filtro, verificamos si el hecho cumple al menos uno de ese tipo.
        return filtros.stream()
                .collect(Collectors.groupingBy(CriterioDePertenencia::getClass))
                .values()
                .stream()
                .allMatch(grupo -> grupo.stream().anyMatch(f -> f.cumpleUno(this)));
    }


    private boolean cumpleElTipoDeFiltro(CriterioDePertenencia unFiltro, List<CriterioDePertenencia> filtros) {
        return filtros.stream()
                .filter( otroFiltro -> this.coincidenTipos(otroFiltro, unFiltro)) //esto nos deja los criterios que tenga el mismo tipo que un criterio
                .anyMatch(criterioFiltrado -> criterioFiltrado.cumpleUno(this));
        // En la lista de criterios del mismo tipo, evalúo cada uno con unHecho, si uno solo cumple -> Devuelve true.
    }

    private Boolean coincidenTipos(CriterioDePertenencia unFiltro, CriterioDePertenencia otroFiltro) {
        return unFiltro.getClass() == otroFiltro.getClass();
    }


}
