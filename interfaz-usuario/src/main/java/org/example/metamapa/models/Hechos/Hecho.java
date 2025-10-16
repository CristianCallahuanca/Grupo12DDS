package org.example.metamapa.models.Hechos;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Entity // Le decimos a JPA que esta clase es una tabla en la BD
public class Hecho {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El ID se auto-genera
    private Long id;

    private String titulo;

    @Column(length = 1000) // Para textos largos
    private String descripcion;

    private String categoria;
    private String latitud;
    private String longitud;
    private LocalDateTime fechaAcontecimiento;
    private String etiqueta;
    private String contribuyenteID; // El username del usuario que lo creó
    private String origen; // Ej: "WEB", "MOBILE_APP"

    // Si vas a manejar archivos, necesitarías una relación @OneToMany aquí.

}