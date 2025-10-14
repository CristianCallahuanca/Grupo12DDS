package org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado;

import jakarta.persistence.criteria.Join;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import jakarta.persistence.*;
import org.example.metamapa.gestordatos.models.entidades.Ubicacion;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@DiscriminatorValue("AREA_VISIBLE")
public class PorAreaVisible extends CondicionDeFiltrado {

    @Column(name = "norte_bound")
    private Double norte;

    @Column(name = "sur_bound")
    private Double sur;

    @Column(name = "este_bound")
    private Double este;

    @Column(name = "oeste_bound")
    private Double oeste;

    public PorAreaVisible(Double norte, Double sur, Double este, Double oeste) {
        this.norte = norte;
        this.sur = sur;
        this.este = este;
        this.oeste = oeste;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        // Para la lógica en memoria (si la necesitás)
        Double latitud = unHecho.getUbicacion().getLatitud();
        Double longitud = unHecho.getUbicacion().getLongitud();

        return latitud >= sur && latitud <= norte &&
                longitud >= oeste && longitud <= este;
    }

    @Override
    public Specification<Hecho> toSpecification() {
        return (root, query, cb) -> {
            // Join con la entidad Ubicacion
            Join<Hecho, Ubicacion> ubicacionJoin = root.join("ubicacion");

            return cb.and(
                    cb.between(ubicacionJoin.get("latitud"), sur, norte),
                    cb.between(ubicacionJoin.get("longitud"), oeste, este)
            );
        };
    }
}