package org.example.metamapa.agregador.models.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "origenes_reales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrigenReal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoFuente tipoFuente;

}
