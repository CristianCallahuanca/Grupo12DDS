package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;


@Entity
@NoArgsConstructor
@DiscriminatorValue("CARGA_DESDE")
public class PorFechaCargaDesde extends CondicionDeFiltrado{

    @Column(name = "desde")
    private LocalDateTime desde;

    public PorFechaCargaDesde(LocalDateTime fecha) {
        desde = fecha;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaCarga().isAfter(desde) || unHecho.getFechaCarga().isEqual(desde));
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaCarga"), desde);
    }
}



