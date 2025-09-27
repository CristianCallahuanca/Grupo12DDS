package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.Join;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Coleccion;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@Entity
@NoArgsConstructor
@DiscriminatorValue("POR_COLECCION")
public class PorColeccion extends CondicionDeFiltrado{

    @Column(name = "handle")
    private String handle;

    public PorColeccion(String handle){
        this.handle = handle;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return true;
    }

    @Override
    public Specification<Hecho> toSpecification() {

        return (root, query, cb) -> {
            // Join hacia la tabla puente
            Join<Hecho, HechoDeColeccion> hechoColeccionJoin = root.join("hechosDeColeccion");

            // Desde ahí, join con Coleccion
            Join<HechoDeColeccion, Coleccion> coleccionJoin = hechoColeccionJoin.join("coleccion");

            return cb.equal(coleccionJoin.get("handle"), handle);
        };
    }
}

