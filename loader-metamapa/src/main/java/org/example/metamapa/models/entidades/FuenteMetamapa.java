package org.example.metamapa.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fuentes_metamapa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuenteMetamapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombreFuente;

    @Column(nullable = false)
    private String baseUrl;

    private boolean activa = true;

    private LocalDateTime ultimaConsulta;       // null => nunca se consulto

    private Integer cantidadHechosUltima;       // cuantos hechos devolvio la última vez

}

