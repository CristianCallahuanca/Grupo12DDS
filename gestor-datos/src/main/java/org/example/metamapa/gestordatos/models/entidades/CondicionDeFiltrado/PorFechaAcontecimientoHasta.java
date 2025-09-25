package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;



import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ACONTECIMIENTO_HASTA")
public class PorFechaAcontecimientoHasta extends CondicionDeFiltrado{

    @Column(name = "hasta")
    private LocalDateTime hasta;

    public PorFechaAcontecimientoHasta(LocalDateTime fecha) {
        hasta = fecha;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaAcontecimiento().isBefore(hasta) || unHecho.getFechaAcontecimiento().isEqual(hasta));
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaAcontecimiento"), hasta);
    }
}


