package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;


@Entity
@NoArgsConstructor
@DiscriminatorValue("PORTITULO")
public class PorTitulo extends CondicionDeFiltrado{

    @Column(name = "titulo_buscado")
    private String tituloBuscado;

    public PorTitulo(String titulo) {
        this.tituloBuscado = titulo;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  Objects.equals(unHecho.getTitulo(), tituloBuscado);
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> cb.like(
                cb.lower(root.get("titulo")),
                "%" + tituloBuscado.toLowerCase() + "%"
        );
    }


}
