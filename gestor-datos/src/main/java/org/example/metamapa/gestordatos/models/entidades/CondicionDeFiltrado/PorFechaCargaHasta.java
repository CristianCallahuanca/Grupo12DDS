package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@DiscriminatorValue("CARGA_HASTA")
public class PorFechaCargaHasta extends CondicionDeFiltrado{

    @Column(name = "hasta")
    private LocalDateTime hasta;

    public PorFechaCargaHasta(LocalDateTime fecha) {
        hasta = fecha;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaCarga().isBefore(hasta) || unHecho.getFechaCarga().isEqual(hasta));
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaCarga"), hasta);
    }
}
