package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "sinonimos")
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

