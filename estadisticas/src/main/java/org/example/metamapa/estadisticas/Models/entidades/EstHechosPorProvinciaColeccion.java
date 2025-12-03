package org.example.metamapa.estadisticas.Models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "est_hechos_por_provincia_coleccion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstHechosPorProvinciaColeccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;

    private String coleccionHandle;
    private String coleccionTitulo;

    private String provincia;

    private Integer cantidadHechos;
}
