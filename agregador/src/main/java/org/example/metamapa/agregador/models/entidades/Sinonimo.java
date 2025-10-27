package org.example.metamapa.agregador.models.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sinonimos")
@Data
public class Sinonimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String palabra;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
