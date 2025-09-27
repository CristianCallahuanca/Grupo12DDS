package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoDeEdicion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
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

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long hecho_id;

        @Column(name = "titulo")
        private String titulo;

        @Column(name = "descripcion")
        private String descripcion;

        @Column(name = "categoria")
        private String categoria;

        @OneToOne
        @JoinColumn(name = "ubicacion_id")
        private Ubicacion ubicacion;

        @Column(name = "facha_acontecimiento")
        private LocalDateTime fechaAcontecimiento;

        @Column(name = "fecha_carga")
        private LocalDateTime fechaCarga;

        @Enumerated(EnumType.STRING)
        private EstadoHecho estadoHecho;

        @Enumerated(EnumType.STRING)
        private EstadoDeEdicion estadoEdicionHecho;

        @ElementCollection
        @CollectionTable(
                name = "hecho_multimedia",
                joinColumns = @JoinColumn(name = "hecho_id")
        )
        @Column(name = "archivoMultimedia")
        private List<String> archivosMultimedia;

        @Column(name = "etiqueta")
        private String etiqueta;

        @JoinColumn(name = "contribuyente_id")
        @ManyToOne
        private ContribuyenteRegistrado contribuyente;

        @ElementCollection(targetClass = Origen.class)
        @CollectionTable(
                name = "hecho_origenes",
                joinColumns = @JoinColumn(name = "hecho_id")
        )
        @Column(name = "origen")
        @Enumerated(EnumType.STRING)
        private List<Origen> origenes;

        @Column(name = "sin_categorizar")
        private Boolean sinCategorizar;

        @OneToMany(mappedBy = "hecho")
        private List<HechoDeColeccion> hechosDeColeccion;

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
    } // esto no iría en un service o alguna cosa así??

    public boolean puedeSerEditado() {
        return this.origenes.contains("Dinamica")  && //hay que ver que sea registrado
                ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) <= 7;
    }

    public void printHecho() {
        System.out.println("ID: " + this.hecho_id);
        System.out.println("Título: " + this.titulo);
        System.out.println("Descripción: " + this.descripcion);
        System.out.println("Categoría: " + this.categoria);

        if (this.ubicacion != null) {
            System.out.println("Ubicación: " + this.ubicacion.toString());
        }

        System.out.println("Fecha de acontecimiento: " + this.fechaAcontecimiento);
        System.out.println("Fecha de carga: " + this.fechaCarga);
        System.out.println("Estado del hecho: " + this.estadoHecho);
        System.out.println("Estado de edición: " + this.estadoEdicionHecho);

        System.out.println("Archivos multimedia: ");
        if (this.archivosMultimedia != null) {
            this.archivosMultimedia.forEach(System.out::println);
        }

        System.out.println("Etiqueta: " + this.etiqueta);

        if (this.contribuyente != null) {
            System.out.println("Contribuyente: " + this.contribuyente.toString());
        }

        System.out.println("Orígenes: ");
        if (this.origenes != null) {
            this.origenes.forEach(System.out::println);
        }

        System.out.println("Sin categorizar: " + this.sinCategorizar);
    }


}
