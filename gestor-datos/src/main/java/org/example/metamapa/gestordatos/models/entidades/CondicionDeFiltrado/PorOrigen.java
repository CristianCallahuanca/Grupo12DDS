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
            Join<Hecho, Origen> join = root.join("origenes");
            return cb.equal(join, unOrigen);
        };
    }





}
