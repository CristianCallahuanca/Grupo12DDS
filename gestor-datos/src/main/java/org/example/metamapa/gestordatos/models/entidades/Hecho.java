package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoDeEdicion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "categoria")
    private String categoria;

    @OneToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(name = "fecha_acontecimiento")
    private LocalDateTime fechaAcontecimiento;

    @Column(name = "fecha_carga", nullable = false)
    private LocalDateTime fechaCarga;

    @Enumerated(EnumType.STRING)
    private EstadoHecho estadoHecho;

    @Enumerated(EnumType.STRING)
    private EstadoDeEdicion estadoEdicionHecho;

    @ElementCollection
    @CollectionTable(name = "hecho_multimedia", joinColumns = @JoinColumn(name = "hecho_id"))
    @Column(name = "archivoMultimedia")
    private List<String> archivosMultimedia = new ArrayList<>();

    @Column(name = "etiqueta")
    private String etiqueta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuyente_id")
    private ContribuyenteRegistrado contribuyente;

    @ElementCollection(targetClass = Origen.class)
    @CollectionTable(name = "hecho_origenes", joinColumns = @JoinColumn(name = "hecho_id"))
    @Column(name = "origen")
    @Enumerated(EnumType.STRING)
    private List<Origen> origenes = new ArrayList<>();

    @Column(name = "sin_categorizar")
    private Boolean sinCategorizar = false;

    @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HechoDeColeccion> hechosDeColeccion = new ArrayList<>();

    public Hecho(String titulo,
                 String descripcion,
                 String categoria,
                 Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,
                 String etiqueta) {

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


    public void marcarComoNoVisible() { this.estadoHecho = EstadoHecho.NO_VISIBLE; }


    public void agregarOrigen(Origen origen) {
        if (origen == null) return;
        if (this.origenes == null) this.origenes = new ArrayList<>();
        if (!this.origenes.contains(origen)) this.origenes.add(origen);
    }


    public boolean puedeSerEditadoPor(ContribuyenteRegistrado autor) {
        if (autor == null || this.contribuyente == null) return false;
        boolean esAutor = this.contribuyente.getId().equals(autor.getId());
        boolean esDinamica = this.origenes != null && this.origenes.contains(Origen.DINAMICA);
        boolean dentroDePlazo = ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) <= 7;
        return esAutor && esDinamica && dentroDePlazo;
    }

    public void editarCon(Hecho cambios, ContribuyenteRegistrado autor) {
        if (!puedeSerEditadoPor(autor)) {
            throw new IllegalStateException("El hecho ya no puede ser editado o el autor no está autorizado.");
        }

        this.titulo = cambios.getTitulo();
        this.descripcion = cambios.getDescripcion();
        this.categoria = cambios.getCategoria();
        this.ubicacion = cambios.getUbicacion();
        this.etiqueta = cambios.getEtiqueta();
        this.archivosMultimedia = (cambios.getArchivosMultimedia() != null)
                ? new ArrayList<>(cambios.getArchivosMultimedia())
                : new ArrayList<>();
        this.fechaAcontecimiento = cambios.getFechaAcontecimiento();
        this.estadoEdicionHecho = EstadoDeEdicion.EDITADO;

    }




}
