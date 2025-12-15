package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.models.entidades.enums.TipoFuente;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
@Entity
@NoArgsConstructor
@DiscriminatorValue("TIPO_FUENTE")
public class PorTipoFuente extends CondicionDeFiltrado {

    @Enumerated(EnumType.STRING)
    private TipoFuente unTipoFuente;

    public PorTipoFuente(TipoFuente tipoFuente) {
        this.unTipoFuente = tipoFuente;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getTipoFuente() == unTipoFuente;
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> {
            log.debug("PorTipoFuente.toSpecification -> unTipoFuente = {}", unTipoFuente);
            return cb.equal(root.get("tipoFuente"), unTipoFuente);
        };
    }
}

