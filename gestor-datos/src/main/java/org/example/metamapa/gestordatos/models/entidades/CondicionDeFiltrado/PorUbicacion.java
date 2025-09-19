package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import org.example.metamapa.gestordatos.models.entidades.Ubicacion;


@Entity
@NoArgsConstructor
@DiscriminatorValue("UBICACION")
public class PorUbicacion extends CondicionDeFiltrado{

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    @Column(name = "ubicacion_buscada")
    private Ubicacion ubicacionBuscada;

    public PorUbicacion(Ubicacion ubicacion1) {
        ubicacionBuscada = ubicacion1;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return unHecho.getUbicacion().equals(ubicacionBuscada);

    }
}
