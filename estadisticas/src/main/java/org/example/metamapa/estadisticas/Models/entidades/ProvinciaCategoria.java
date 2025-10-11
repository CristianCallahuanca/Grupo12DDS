package org.example.metamapa.estadisticas.Models.entidades;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ProvinciaCategoria {
    // Getters y Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cantidad")
    private Long cantidad;

    // Constructores
    public ProvinciaCategoria() {}

    public ProvinciaCategoria(String categoria, String provincia, Long cantidad) {
        this.categoria = categoria;
        this.provincia = provincia;
        this.cantidad = cantidad;
    }

    public void setId(Long id) { this.id = id; }

    public void setCategoria(String categoria) { this.categoria = categoria; }

    public void setProvincia(String provincia) { this.provincia = provincia; }

    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
}