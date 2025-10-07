package org.example.metamapa.estatico.models.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HechoCrudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id_hecho;

    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;

    private boolean enviado;
    private LocalDateTime fechaEnvio;

    // private String fuenteOrigen;
}
