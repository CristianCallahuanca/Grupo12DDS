package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import jakarta.persistence.*;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.data.jpa.domain.Specification;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Table(name = "condicion_filtrado")
public abstract class CondicionDeFiltrado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public abstract boolean cumpleUno(Hecho unHecho);

    public abstract Specification<Hecho> toSpecification();
}
