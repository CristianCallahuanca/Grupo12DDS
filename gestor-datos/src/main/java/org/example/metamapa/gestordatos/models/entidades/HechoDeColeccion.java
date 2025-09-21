package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hecho_de_coleccion")
public class HechoDeColeccion {

    /*HechoDeColeccion(Hecho hecho, Boolean esConsensuado){
        this.hecho = hecho;
        this.esConsensuado = esConsensuado;
    }*/

    //estoy probando no más
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "hecho_id")
    @ManyToOne
    private Hecho hecho;

    @Column(name= "es_consensuado")
    private Boolean esConsensuado;

}
