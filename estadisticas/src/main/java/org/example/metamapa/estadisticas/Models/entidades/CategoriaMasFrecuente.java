package org.example.metamapa.estadisticas.Models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categoria_mas_frecuente")
public class CategoriaMasFrecuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "cantidad")
    private long cantidad;


    public CategoriaMasFrecuente(String categoria, long cantidad) {
        this.categoria = categoria;
        this.cantidad = cantidad;
    }
}