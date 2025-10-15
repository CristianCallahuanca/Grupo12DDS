package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@Entity
@NoArgsConstructor
@DiscriminatorValue("CATEGORIA")
public class PorCategoria extends CondicionDeFiltrado {

    @Column(name = "categoria_deseada")
    private String categoriaDeseada;

    public PorCategoria(String keyPhrase){
        this.categoriaDeseada = keyPhrase;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getCategoria(), categoriaDeseada);
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("categoria")), "%" + categoriaDeseada.toLowerCase() + "%");
    } //no se que tan correcto es pero va a funcionar

}
