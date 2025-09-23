package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@DiscriminatorValue("SIN_CATEGORIZAR")
public class PorSinCategorizar extends CondicionDeFiltrado{

    @Column(name = "sin_categorizar")
    private boolean sinCategorizar;

    public PorSinCategorizar(boolean situacion){
        this.sinCategorizar = situacion;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getSinCategorizar(), sinCategorizar);
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> cb.equal(root.get("sinCategorizar"), sinCategorizar);
    }

}
