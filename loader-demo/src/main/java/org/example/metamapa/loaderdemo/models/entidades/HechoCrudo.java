package org.example.metamapa.loaderdemo.models.entidades;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
public class HechoCrudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loaderId;

    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String etiqueta;
    private LocalDate fecha;
    private String origen;
    private LocalDate fechaIngesta;

    private boolean enviado = false;
    private LocalDateTime fechaEnvio;
}
