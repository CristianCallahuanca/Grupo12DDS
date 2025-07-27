package AdministracionDeHechos;
import Fuentes.Fuente;
import Infraestructura.Repositorios.HechoRepositorio;
import Persona.Contribuyente.Contribuyente;
import Servicios.ServicioDeIdentificacion;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.example.Main.logger;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Es necesario para los algoritmos de concenso
public class Hecho {
    @EqualsAndHashCode.Include
    private String titulo;
    @EqualsAndHashCode.Include
    private String descripcion;
    @EqualsAndHashCode.Include
    private String categoria;
    @EqualsAndHashCode.Include
    private Ubicacion ubicacion;
    @EqualsAndHashCode.Include
    private List<String> archivosMultimedia;
    @EqualsAndHashCode.Include
    private String etiqueta;
    @EqualsAndHashCode.Include
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Contribuyente contribuyente;
    private Origen origen;
    private Fuente fuente;
    private int id_hecho;
    private EstadoHecho estadoHecho;
    private EstadoEdicionHecho estadoEdicionHecho;

    
    public Hecho(String titulo, String descripcion, String categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,String etiqueta) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.etiqueta = etiqueta;
        this.estadoHecho = EstadoHecho.EN_REVISION;
        this.estadoEdicionHecho = EstadoEdicionHecho.NO_EDITADO;
        this.fechaCarga = LocalDateTime.now();
        this.id_hecho = ServicioDeIdentificacion.getInstancia().generarIDHecho();
    }

    public void setOrigen(Origen unOrigen) {
        this.origen = unOrigen;
        if (unOrigen != Origen.ESTATICA) {HechoRepositorio.getInstancia().guardar(this);}
    }

    //METODOS DE HECHOS
    public void marcarComoNoVisible() {
        this.estadoHecho = EstadoHecho.NO_VISIBLE;
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
            this.estadoEdicionHecho = EstadoEdicionHecho.EDITADO;
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


}
