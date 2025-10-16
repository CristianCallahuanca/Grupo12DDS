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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hecho_id", nullable = false)
    private Hecho hecho;

    @Column(name= "consensuado", nullable = false)
    private boolean consensuado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coleccion_id", nullable = false)
    private Coleccion coleccion;

    public HechoDeColeccion(Hecho hecho, boolean esConsensuado) {
        this.hecho = hecho;
        this.consensuado = esConsensuado;
    }
}
