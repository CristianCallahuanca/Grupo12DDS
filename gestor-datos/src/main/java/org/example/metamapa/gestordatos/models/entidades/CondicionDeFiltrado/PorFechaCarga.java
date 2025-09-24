package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;


@Entity
@NoArgsConstructor
@DiscriminatorValue("CARGA")
public class PorFechaCarga extends CondicionDeFiltrado{

    @Column(name = "desde")
    private LocalDateTime desde;

    @Column(name = "hasta")
    private LocalDateTime hasta;

    public PorFechaCarga(LocalDateTime fa1, LocalDateTime fc1) {
        desde = fa1;
        hasta = fc1;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaCarga().isAfter(desde) || unHecho.getFechaCarga().isEqual(desde)) &&
                (unHecho.getFechaCarga().isBefore(hasta) || unHecho.getFechaCarga().isEqual(hasta));
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> cb.between(root.get("fechaCarga"), desde, hasta);
    }

}
