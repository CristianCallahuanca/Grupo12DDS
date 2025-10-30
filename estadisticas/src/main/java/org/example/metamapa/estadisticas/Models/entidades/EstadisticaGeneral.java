package org.example.metamapa.estadisticas.Models.entidades;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "tipo_estadistica")
    private String tipoEstadistica;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cantidad")
    private String cantidad;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "hora")
    private String hora;


    public EstadisticaGeneral(String tipo_estadistica, String categoria, String provincia, String cantidad,
                              String titulo, String hora) {

        this.fecha = LocalDateTime.now();
        this.tipoEstadistica = tipo_estadistica;
        this.categoria = categoria;
        this.provincia = provincia;
        this.cantidad = cantidad;
        this.titulo = titulo;
        this.hora = hora;
    }

}
