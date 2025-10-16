package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.gestordatos.conversores.AlgoritmoConsensoAttributeConverter;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.ModosNavegacion.ModoNavegacion;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coleccion")
public class Coleccion {

    @Id
    @Column(name = "handle", nullable = false, unique = true)
    private String handle;

    @ElementCollection(targetClass = Origen.class)
    @CollectionTable(name = "coleccion_origenes", joinColumns = @JoinColumn(name = "coleccion_id"))
    @Column(name = "origen")
    @Enumerated(EnumType.STRING)
    private List<Origen> origenes = new ArrayList<>();

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "coleccion_id")
    private List<CondicionDeFiltrado> criterios = new ArrayList<>();

    @OneToMany(mappedBy = "coleccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HechoDeColeccion> hechosColeccion = new ArrayList<>();

    @Convert(converter = AlgoritmoConsensoAttributeConverter.class)
    @Column(name = "algoritmoConsenso")
    private AlgoritmoConsenso algoritmo;

    public Coleccion(String handle,
                     List<Origen> origenes,
                     String titulo,
                     String descripcion,
                     List<CondicionDeFiltrado> criterios,
                     AlgoritmoConsenso algoritmo) {
        this.handle = handle;
        this.origenes = (origenes != null) ? new ArrayList<>(origenes) : new ArrayList<>();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios = (criterios != null) ? new ArrayList<>(criterios) : new ArrayList<>();
        this.algoritmo = algoritmo;
    }


    public List<Hecho> obtenerHechos() {
        return hechosColeccion.stream().map(HechoDeColeccion::getHecho).toList();
    }

    public List<Hecho> obtenerHechosVisibles() {
        return obtenerHechos().stream()
                .filter(h -> h.getEstadoHecho() != EstadoHecho.NO_VISIBLE)
                .toList();
    }

    public List<Hecho> obtenerHechosConsensuados() {
        if (algoritmo != null) {
            return hechosColeccion.stream()
                    .filter(HechoDeColeccion::isConsensuado)
                    .map(HechoDeColeccion::getHecho)
                    .toList();
        }
        return obtenerHechosVisibles();
    }

    public List<Hecho> obtenerHechosPorModo(ModoNavegacion modo) {
        return modo.aplicarModoDeNavegacion(this);
    }

    public void aplicarConsenso() {
        if (algoritmo != null) algoritmo.consensuarHechos(this.hechosColeccion);
    }


    private HechoDeColeccion wrap(Hecho h) {
        return new HechoDeColeccion(h, false);
    }

    public void reemplazarHechoDeColeccion(List<Hecho> hechos) {
        List<HechoDeColeccion> nuevos = hechos.stream().map(this::wrap).toList();
        this.hechosColeccion.clear();
        this.hechosColeccion.addAll(nuevos);
    }

    public void agregarHechos(List<Hecho> hechos) {
        List<HechoDeColeccion> nuevos = hechos.stream().map(this::wrap).toList();
        this.hechosColeccion.addAll(nuevos);
    }

    public void agregarNuevaFuente(Origen nuevaFuente) {
        if (nuevaFuente == null) return;
        if (this.origenes == null) this.origenes = new ArrayList<>();
        if (!this.origenes.contains(nuevaFuente)) this.origenes.add(nuevaFuente);
    }

    public void eliminarFuente(Origen fuente) {
        if (this.origenes != null) this.origenes.remove(fuente);
    }


}
