package org.example.metamapa.gestordatos.models.entidades;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoDeEdicion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoDeEdicion;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hecho")
public class Hecho {
    // acordarme de cambiar origen a orígenes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "categoria")
    private String categoria;

    @OneToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;


    private LocalDateTime fechaAcontecimiento;

    private LocalDateTime fechaCarga;

    private EstadoHecho estadoHecho;

    private EstadoDeEdicion estadoEdicionHecho;

    private List<String> archivosMultimedia;

    private String etiqueta;

    private ContribuyenteRegistrado contribuyente;

    private List<Origen> origenes;

    private Boolean sinCategorizar;

    public Hecho(String titulo, String descripcion, String categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,String etiqueta) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.etiqueta = etiqueta;
        this.estadoHecho = EstadoHecho.EN_REVISION;
        this.estadoEdicionHecho = EstadoDeEdicion.NO_EDITADO;
        this.fechaCarga = LocalDateTime.now();
    }

    public void agregarOrigen(Origen unOrigen){
        this.origenes.add(unOrigen);
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
            this.estadoEdicionHecho = EstadoDeEdicion.EDITADO;
            //NO cambiar contribuyente, origen ni fecha de carga
        } else {
            throw new IllegalStateException("El hecho ya no puede ser editado.");
        }
    }

    public boolean puedeSerEditado() {
        return this.origenes.contains(Origen.DINAMICA)  && //hay que ver que sea registrado
                ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) <= 7;
    }

}
