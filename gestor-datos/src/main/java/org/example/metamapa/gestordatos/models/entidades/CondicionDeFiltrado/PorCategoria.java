package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@Entity
@NoArgsConstructor
@DiscriminatorValue("CATEGORIA")
public class PorCategoria extends CondicionDeFiltrado {

    @Column(name = "categoria_deseada")
    private String categoriaDeseada;

    public PorCategoria(String keyPhrase) {
        this.categoriaDeseada = keyPhrase;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        // Comparar por nombre, no por objeto
        return unHecho.getCategoria() != null &&
                unHecho.getCategoria().getNombre().equalsIgnoreCase(categoriaDeseada);
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("categoria").get("nombre")), "%" + categoriaDeseada.toLowerCase() + "%");
    }
}
