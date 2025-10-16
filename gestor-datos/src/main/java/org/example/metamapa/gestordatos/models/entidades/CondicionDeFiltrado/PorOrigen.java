package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;


@Entity
@NoArgsConstructor
@DiscriminatorValue("ORIGEN")
public class PorOrigen extends CondicionDeFiltrado {

    @Enumerated(EnumType.STRING)
    private Origen unOrigen;

    public PorOrigen(Origen origen) {
        this.unOrigen = origen;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho){
        return unHecho.getOrigenes().contains(unOrigen);
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> {
            System.out.println(" Ejecutando filtro explícito con FETCH JOIN sobre origenes: " + unOrigen);

            root.fetch("origenes", JoinType.INNER);
            Join<Object, Object> join = root.join("origenes", JoinType.INNER);


            System.out.println("JOIN creado correctamente con unOrigen=" + unOrigen);

            return cb.equal(join, unOrigen);
        };
    }




}
