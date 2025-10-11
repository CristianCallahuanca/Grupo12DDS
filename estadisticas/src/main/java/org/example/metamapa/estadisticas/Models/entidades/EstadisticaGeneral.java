package org.example.metamapa.estadisticas.Models.entidades;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estadistica_general")
public class EstadisticaGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "primera_id")
    private HechosPorProvincia hechosProvincia;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "segunda_id")
    private CategoriaMasFrecuente categoriaMasFrec;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "estadistica_id")
    private List<ProvinciaMasFrecuentePorCategoria> provinciaPorCat;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    public EstadisticaGeneral(HechosPorProvincia hechosProvincia, CategoriaMasFrecuente categoriaMasFrec,
                              List<ProvinciaMasFrecuentePorCategoria> frcPorCategoria){
        this.fecha = LocalDateTime.now();
        this.hechosProvincia = hechosProvincia;
        this.categoriaMasFrec = categoriaMasFrec;
        this.provinciaPorCat = frcPorCategoria;
    }

}
