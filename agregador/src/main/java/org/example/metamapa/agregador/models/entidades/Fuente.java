package org.example.metamapa.agregador.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fuentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fuente {

    @Id
    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String baseUrl;
}
