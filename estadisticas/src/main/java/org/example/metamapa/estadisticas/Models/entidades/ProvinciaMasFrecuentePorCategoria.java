package org.example.metamapa.estadisticas.Models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ProvinciaMasFrecuentePorCategoria")
public class ProvinciaMasFrecuentePorCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cantidad")
    private long cantidad;

    ProvinciaMasFrecuentePorCategoria(String categoria, String provincia, long cantidad){
        this.categoria = categoria;
        this.provincia = provincia;
        this.cantidad = cantidad;
    }
}
