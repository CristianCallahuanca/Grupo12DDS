package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ESTADO")
public class PorEstado extends CondicionDeFiltrado {

    @Enumerated(EnumType.STRING)
    private EstadoHecho unEstado;

    public PorEstado(EstadoHecho unEstado){
        this.unEstado = unEstado;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  unHecho.getEstadoHecho().equals(this.unEstado);
    }

}
