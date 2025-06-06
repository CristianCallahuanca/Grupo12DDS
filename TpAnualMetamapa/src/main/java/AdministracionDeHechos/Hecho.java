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

    // Getters

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public LocalDateTime getFechaAcontecimiento() {
        return fechaAcontecimiento;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public boolean esVisible() {
        return visible;
    }

    public Origen getOrigen() {
        return origen;
    }

    public List<String> getArchivosMultimedia() {
        return archivosMultimedia;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public Contribuyente getContribuyente() {
        return contribuyente;
    }


    public void marcarComoNoVisible() {
        this.visible = false;
    }

    public void editarHecho(String nuevoTitulo, String nuevaDescripcion) {

    }

    /*public boolean puedeSerEditado() {
        return this.origen == Origen.DINAMICA &&
                ChronoUnit.DAYS.between(fechaCarga, LocalDateTime.now()) <= 7;
    }*/

}
