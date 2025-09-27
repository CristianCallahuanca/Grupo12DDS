package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.conversores.AlgoritmoConsensoAttributeConverter;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorOrigen;
import org.example.metamapa.gestordatos.models.entidades.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.entidades.ModosNavegacion.ModoNavegacion;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;

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
    @CollectionTable(
            name = "coleccion_origenes",
            joinColumns = @JoinColumn(name = "coleccion_id")
    )
    @Column(name = "origen")
    @Enumerated(EnumType.STRING)
    private List<Origen> origenes;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) //a chequear esto
    @JoinColumn(name = "coleccion_id")
    private List<CondicionDeFiltrado> criterios;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "coleccion_id") //hay que especificar como se tiene que llamar la columna de la tabla hecho que apunta a coleccion
    private List<HechoDeColeccion> hechosColeccion;

    @Convert(converter = AlgoritmoConsensoAttributeConverter.class)
    @Column(name = "algoritmoConsenso")
    private AlgoritmoConsenso algoritmo;


    public Coleccion(String handle, List<Origen> origenes, String titulo, String descripcion, List<CondicionDeFiltrado> criterios, AlgoritmoConsenso algoritmo) {
        this.handle = handle;
        this.origenes = origenes;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios = criterios;
        this.algoritmo = algoritmo;

        /*List<PorOrigen> origenesJoaco = origenes.stream()
                .map(unOrigen -> new PorOrigen(unOrigen))
                .toList();

        this.criterios.addAll(origenesJoaco);*/

    }

    public void insertarHechos(List <HechoDeColeccion> unosHechos) {this.hechosColeccion.addAll(unosHechos);}



    public List<Hecho> obtenerHechosVisibles() {
        return obtenerHechos().stream().filter(hecho->hecho.getEstadoHecho() != EstadoHecho.NO_VISIBLE).toList();
    }

    public List<Hecho> obtenerHechosConsensuados(){
        if(algoritmo != null){
            return hechosColeccion.stream().filter(HechoDeColeccion::getEsConsensuado)
                    .map(HechoDeColeccion::getHecho).toList();
        } else {
            return obtenerHechosVisibles(); // para que devuelva todos los hechos si no tiene algoritmo de consenso
        }
    }

    public List <Hecho> obtenerHechosPorModo(ModoNavegacion algunModo)
    {
        return algunModo.aplicarModoDeNavegacion(this);
    }

    public void aplicarConsenso() {
            this.algoritmo.consensuarHechos(this.hechosColeccion);
    }

    public List<Hecho> obtenerHechos(){
        return hechosColeccion.stream().map(e -> e.getHecho()).toList();
    }

    private HechoDeColeccion hechoToHechoDeColeccion(Hecho unHecho){
        return new HechoDeColeccion(unHecho, false);
    }

    public void reemplazarHechoDeColeccion(List<Hecho> hechos){
        List <HechoDeColeccion> paraGuardar = hechos.stream().map(h -> hechoToHechoDeColeccion(h) ).toList();
        this.setHechosColeccion(paraGuardar);
    }

    public void agregarHechos(List<Hecho> hechos){
        List<HechoDeColeccion> nuevosHechosDeColeccion = hechos.stream().map(h -> hechoToHechoDeColeccion(h)).toList();
        this.hechosColeccion.addAll(nuevosHechosDeColeccion);
    }

    public void agregarNuevaFuente(Origen nuevaFuente){
        this.origenes.add(nuevaFuente);
    }

    public void eliminarFuente(Origen fuente){
        this.origenes.remove(fuente);
    }


/*
    //Solo para tests
    public void imprimirHechos(List<Hecho> unosHechos) {
        unosHechos.forEach(unHecho -> unHecho.imprimirHecho());
    }

    //Para que los usuarios puedan navegar en la colección
    private List<Hecho> obtenerHechosFiltrados(List<CriterioDePertenencia> filtros) throws IOException {
        return ServicioFiltradorDeHechos.filtrarHechos(this.hechos ,filtros);
    }*/


}
