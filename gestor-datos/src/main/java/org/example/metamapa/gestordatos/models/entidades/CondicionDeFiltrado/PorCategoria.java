package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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

}
