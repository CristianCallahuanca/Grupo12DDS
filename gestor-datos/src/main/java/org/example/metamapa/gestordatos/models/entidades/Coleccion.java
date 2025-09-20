package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.conversores.AlgoritmoConsensoAttributeConverter;
import org.example.metamapa.gestordatos.models.entidades.Consenso.AlgoritmoConsenso;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;

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
    private List<Origen> fuentes;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) //a chequear esto
    @JoinColumn(name = "coleccion_id")
    private List<CondicionDeFiltrado> criterios;

    @OneToMany
    @JoinColumn(name = "coleccion_id") //hay que especificar como se tiene que llamar la columna de la tabla hecho que apunta a coleccion
    private List<HechoDeColeccion> hechosColeccion; //TODO habría que cambiarlo a hechos de coleccion

    @Convert(converter = AlgoritmoConsensoAttributeConverter.class)
    @Column(name = "algoritmoConsenso")
    private AlgoritmoConsenso algoritmo;


    public void consensuarHechos(){
        //this.algoritmoDeConsenso.esConsensuado(this.hechos);
    }
    /*

    public Coleccion(List<Origen> fuentes, String titulo, String descripcion, List<CriterioDePertenencia> criterios) throws IOException {
        this.fuentes = fuentes;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios = criterios;
        this.handle = ServicioDeIdentificacion.getInstancia().generarHandle();
        ServicioDeAgregacion.getInstancia().primeraCarga(this);
    }

    public void insertarHechos(List <Hecho> unosHechos) {this.hechos.addAll(unosHechos);}

    public List <Hecho> obtenerHechosPorModo(ModoNavegacion algunModo)
    {
        return algunModo.aplicarModoDeNavegacion(this.obtenerHechosVisibles(), this.algoritmoDeConsenso);
    }


    public List<Hecho> obtenerHechosVisibles() {
        return hechos.stream().filter(hecho->hecho.getEstadoHecho() != EstadoHecho.NO_VISIBLE).toList();
    }


    //Solo para tests
    public void imprimirHechos(List<Hecho> unosHechos) {
        unosHechos.forEach(unHecho -> unHecho.imprimirHecho());
    }

    //Para que los usuarios puedan navegar en la colección
    private List<Hecho> obtenerHechosFiltrados(List<CriterioDePertenencia> filtros) throws IOException {
        return ServicioFiltradorDeHechos.filtrarHechos(this.hechos ,filtros);
    }*/


}
