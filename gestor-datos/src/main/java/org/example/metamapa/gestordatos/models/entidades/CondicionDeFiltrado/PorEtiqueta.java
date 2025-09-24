package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ETIQUETA")
public class PorEtiqueta extends CondicionDeFiltrado {

    @Column(name = "etiqueta")
    private String etiquetaDeseada;

    public PorEtiqueta(String etiquetaDeseada) {
        this.etiquetaDeseada = etiquetaDeseada;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getEtiqueta(), etiquetaDeseada);
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> cb.equal(root.get("etiqueta"), etiquetaDeseada);
    }

}
