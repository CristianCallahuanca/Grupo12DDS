package org.example.metamapa.estadisticas.Models.entidades;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private HechosPorProvincia primeraEstadistica;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "segunda_id")
    private CategoriaMasFrecuente segundaEstadistica;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    public EstadisticaGeneral(HechosPorProvincia primera, CategoriaMasFrecuente segunda){
        this.fecha = LocalDateTime.now();
        this.primeraEstadistica = primera;
        this.segundaEstadistica = segunda;
    }

}
