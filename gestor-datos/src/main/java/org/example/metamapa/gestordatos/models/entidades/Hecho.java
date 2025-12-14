package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoDeEdicion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.TipoFuente;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(name = "fecha_acontecimiento")
    private LocalDateTime fechaAcontecimiento;

    @Column(name = "fecha_carga", nullable = false)
    private LocalDateTime fechaCarga;

    @Column(name = "sugerencia_cambio")
    private String sugerenciaCambio;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoFuente", nullable = false)
    private TipoFuente tipoFuente;

    @Column(name = "sin_categorizar")
    private Boolean sinCategorizar = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_id")
    private OrigenReal origenReal;


    @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HechoDeColeccion> hechosDeColeccion = new ArrayList<>();

    public Hecho(String titulo,
                 String descripcion,
                 Categoria categoria,
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


    public boolean puedeSerEditadoPor(ContribuyenteRegistrado autor) {
        if (autor == null || this.contribuyente == null) return false;

        boolean esAutor = this.contribuyente.getUserId() == autor.getUserId();
        boolean esDinamica = this.tipoFuente == TipoFuente.DINAMICA;
        boolean dentroDePlazo = ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) <= 7;

        return esAutor && esDinamica && dentroDePlazo;
    }


    public void editarCon(Hecho cambios, ContribuyenteRegistrado autor) {
        if (!puedeSerEditadoPor(autor)) {
            throw new IllegalStateException("El hecho ya no puede ser editado o el autor no está autorizado.");
        }

        if (cambios.getTitulo() != null) this.titulo = cambios.getTitulo();
        if (cambios.getDescripcion() != null) this.descripcion = cambios.getDescripcion();
        if (cambios.getCategoria() != null) this.categoria = cambios.getCategoria();
        if (cambios.getUbicacion() != null) this.ubicacion = cambios.getUbicacion();
        if (cambios.getEtiqueta() != null) this.etiqueta = cambios.getEtiqueta();

        if (cambios.getFechaAcontecimiento() != null) {
            this.fechaAcontecimiento = cambios.getFechaAcontecimiento();
        }

        this.estadoEdicionHecho = EstadoDeEdicion.EDITADO;
    }





}
