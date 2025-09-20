package org.example.metamapa.agregador.models.entidades;

import dinamico.models.entidades.hecho.EstadoHecho;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.agregador.models.entidades.filtros.FilterCondition;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Enumerated(EnumType.ORDINAL)
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

    @Column( name = "sin_categorizar")
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

    //Ahora los hechos provenientes de fuente estatica se guardan?

    /*public void setOrigen(Origen unOrigen) {
        this.origen = unOrigen;
        if (unOrigen != Origen.ESTATICA) {RepositorioHechos.guardar(this);}
    }*/

    public void marcarComoNoVisible() {
        this.estadoHecho = EstadoHecho.NO_VISIBLE;
    }

    //Se fija si un hecho cumple una lista de criterios y retorna BOOL. NO FILTRA
    public boolean cumpleCondiciones(List<FilterCondition> filtros) {

        // Para cada tipo de filtro, verificamos si el hecho cumple al menos uno de ese tipo.
        return filtros.stream()
                .collect(Collectors.groupingBy(FilterCondition::getClass))
                .values()
                .stream()
                .allMatch(grupo -> grupo.stream().anyMatch(f -> f.cumpleUno(this)));
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
        return this.origenes.contains("Dinamica")  && //hay que ver que sea registrado
                ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now()) <= 7;
    }



}
