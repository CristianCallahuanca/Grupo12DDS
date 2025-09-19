package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@DiscriminatorValue("DESCRIPCION")
public class PorDescripcion extends CondicionDeFiltrado{

    @Column(name = "fraseClave")
    private String fraseClave;

    public PorDescripcion(String keyPhrase){
        this.fraseClave = keyPhrase;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  unHecho.getDescripcion().toLowerCase()
                .contains(this.fraseClave.toLowerCase());
    }
}
