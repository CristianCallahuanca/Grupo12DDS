package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import jakarta.persistence.*;
import org.example.metamapa.gestordatos.models.entidades.Hecho;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Table(name = "condicion_filtrado")
public abstract class CondicionDeFiltrado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public abstract boolean cumpleUno(Hecho unHecho);
}
