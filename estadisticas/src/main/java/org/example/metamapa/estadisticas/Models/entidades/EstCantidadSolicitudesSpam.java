package org.example.metamapa.estadisticas.Models.entidades;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "est_cantidad_solicitudes_spam")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EstCantidadSolicitudesSpam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;

    private Integer cantidadSpam;
}
