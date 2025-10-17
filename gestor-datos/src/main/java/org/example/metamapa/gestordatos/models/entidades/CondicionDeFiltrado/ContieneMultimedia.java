package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;


@Entity
@NoArgsConstructor
@DiscriminatorValue("CONTIENE_MULTIMEDIA")
public class ContieneMultimedia extends CondicionDeFiltrado {

    @Column(name = "contiene_multimedia")
    private Boolean contieneMultimedia;

    public ContieneMultimedia(Boolean contieneMultimedia) {
        this.contieneMultimedia = contieneMultimedia;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return contieneMultimedia; //no anda este metodo es para que no rompa
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> contieneMultimedia ?
                cb.isNotEmpty(root.get("archivosMultimedia")) :
                cb.isEmpty(root.get("archivosMultimedia"));

    }
}
