package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ACONTECIMIENTO_DESDE")
public class PorFechaAcontecimientoDesde extends CondicionDeFiltrado {

    @Column(name = "desde")
    private LocalDateTime desde;

    public PorFechaAcontecimientoDesde(LocalDateTime fecha) {
        desde = fecha;
    }
    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaAcontecimiento().isAfter(desde) || unHecho.getFechaAcontecimiento().isEqual(desde));
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaAcontecimiento"), desde);
    }
}

