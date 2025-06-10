package AdministracionDeHechos;
import Persona.Contribuyente.Contribuyente;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.example.Main.logger;


public class Hecho {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private boolean visible;
    private Origen origen;
    private List<String> archivosMultimedia;
    private String etiqueta;
    private Contribuyente contribuyente;

    public Hecho(String titulo, String descripcion, String categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento, LocalDateTime fechaCarga,String etiqueta){

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.etiqueta = etiqueta;
        this.archivosMultimedia = new ArrayList<>();
        this.origen = Origen.ESTATICA; /*después hay que sacarlo*/
        this.visible = true;
        this.contribuyente = null;
    }


    //GETTERS Y SETTER
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaAcontecimiento() {
        return fechaAcontecimiento;
    }

    public void setFechaAcontecimiento(LocalDateTime fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public boolean getIsVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Origen getOrigen() {
        return origen;
    }

    public void setOrigen(Origen origen) {
        this.origen = origen;
    }

    public List<String> getArchivosMultimedia() {
        return archivosMultimedia;
    }

    public void setArchivosMultimedia(List<String> archivosMultimedia) {
        this.archivosMultimedia = archivosMultimedia;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Contribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
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
            this.archivosMultimedia = cambios.getArchivosMultimedia();
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

}
