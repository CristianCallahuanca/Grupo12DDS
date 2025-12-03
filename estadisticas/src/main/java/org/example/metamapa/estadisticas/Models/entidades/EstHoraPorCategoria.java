package org.example.metamapa.estadisticas.Models.entidades;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "est_hora_por_categoria")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EstHoraPorCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;

    private Long categoriaId;
    private String categoriaNombre;

    private Integer hora;
    private Integer cantidadHechos;
}
