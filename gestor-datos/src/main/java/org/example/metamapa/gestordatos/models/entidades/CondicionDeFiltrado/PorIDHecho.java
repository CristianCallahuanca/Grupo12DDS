package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@DiscriminatorValue("IDHECHO")
public class PorIDHecho extends CondicionDeFiltrado{

    @Column(name = "id_buscado")
    private long idBuscado;

    public PorIDHecho(long id) {
        this.idBuscado = id;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getId() == (idBuscado);
    }
}
