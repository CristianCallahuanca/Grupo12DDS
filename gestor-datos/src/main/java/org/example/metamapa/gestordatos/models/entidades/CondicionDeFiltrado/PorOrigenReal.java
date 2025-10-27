package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ORIGEN_REAL")
public class PorOrigenReal extends CondicionDeFiltrado {

    private String origenReal;

    public PorOrigenReal(String origenReal) {
        this.origenReal = origenReal;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getOrigenReal() != null &&
                unHecho.getOrigenReal().getNombre() != null &&
                unHecho.getOrigenReal().getNombre().equalsIgnoreCase(origenReal);
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("origenReal").get("nombre")), origenReal.toLowerCase());
    }
}