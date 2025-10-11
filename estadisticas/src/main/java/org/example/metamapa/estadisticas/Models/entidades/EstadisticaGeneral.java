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
    private String tipo_estadistica;

    @Column(name = "cantidad_solicitudes_spam")
    private String cantidad_solicitudes_spam;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cantidad")
    private String cantidad;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "cantidad_hechos")
    private String cantidadHechos;

    @Column(name = "hora")
    private String hora;


    public EstadisticaGeneral(String tipo_estadistica,String cantidad_solicitudes_spam, String categoria, String provincia, String cantidad,
                              String titulo, String cantidadHechos, String hora) {

        this.fecha = LocalDateTime.now();
        this.tipo_estadistica = tipo_estadistica;
        this.cantidad_solicitudes_spam = cantidad_solicitudes_spam;
        this.categoria = categoria;
        this.provincia = provincia;
        this.cantidad = cantidad;
        this.titulo = titulo;
        this.cantidadHechos = cantidadHechos;
        this.hora = hora;
    }

}
