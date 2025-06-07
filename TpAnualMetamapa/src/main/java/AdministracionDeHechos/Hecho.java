package AdministracionDeHechos;
import Persona.Contribuyente.Contribuyente;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


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

    public boolean isVisible() {
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

    public void marcarComoNoVisible() {
        this.visible = false;
    }

    public void editarHecho(String nuevoTitulo, String nuevaDescripcion) {

    }

    public boolean puedeSerEditado() {
        return this.origen == Origen.DINAMICA &&
                ChronoUnit.DAYS.between(fechaCarga, LocalDateTime.now()) <= 7;
    }

}
