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
import org.example.metamapa.gestordatos.models.entidades.enums.TipoFuente;

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

    @ElementCollection
    @CollectionTable(name = "coleccion_origenes_reales", joinColumns = @JoinColumn(name = "coleccion_id"))
    @Column(name = "origen_real")
    private List<String> origenesReales = new ArrayList<>();

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "coleccion_id")
    private List<CondicionDeFiltrado> criterios = new ArrayList<>();

    @OneToMany(mappedBy = "coleccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HechoDeColeccion> hechosColeccion = new ArrayList<>();

    @Convert(converter = AlgoritmoConsensoAttributeConverter.class)
    @Column(name = "algoritmoConsenso")
    private AlgoritmoConsenso algoritmo;

    public Coleccion(String handle,
                     TipoFuente tipoFuente,
                     List<String> origenesReales,
                     String titulo,
                     String descripcion,
                     List<CondicionDeFiltrado> criterios,
                     AlgoritmoConsenso algoritmo) {
        this.handle = handle;
        this.origenesReales = (origenesReales != null) ? new ArrayList<>(origenesReales) : new ArrayList<>();
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
            return hechosColeccion.stream()
                    //.filter(HechoDeColeccion::isConsensuado)
                    .filter(hc -> hc.isConsensuado() && hc.getHecho().esVisible())
                    .map(HechoDeColeccion::getHecho)
                    .toList();
    }

    public List<Hecho> obtenerHechosPorModo(ModoNavegacion modo) {
        return modo.aplicarModoDeNavegacion(this);
    }

    public void aplicarConsenso() {
        if (algoritmo != null) {
            algoritmo.consensuarHechos(this.hechosColeccion);
        }
        else {
            this.hechosColeccion.forEach( hc -> hc.setConsensuado(true));
        };
    }


    private HechoDeColeccion wrap(Hecho h) {
        HechoDeColeccion hc = new HechoDeColeccion(h, false);
        hc.setColeccion(this);
        return hc;
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

    public boolean coincideCon(Hecho hecho) {
        boolean coincidePorOrigen = origenesReales.isEmpty() || origenesReales.contains(hecho.getOrigenReal());
        return coincidePorOrigen;
    }

    public void removerHechosPorOrigenes(List<String> origenesAEliminar) {
        if (origenesAEliminar == null || origenesAEliminar.isEmpty()) return;

        this.hechosColeccion.removeIf(hdc ->
                origenesAEliminar.contains(hdc.getHecho().getOrigenReal())
        );

    }


    public void agregarOrigenReal(String origen) {
        if (origen == null || origen.isBlank()) return;
        if (!origenesReales.contains(origen)) origenesReales.add(origen);
    }

    public void eliminarOrigenReal(String origen) {
        origenesReales.remove(origen);
    }


}
