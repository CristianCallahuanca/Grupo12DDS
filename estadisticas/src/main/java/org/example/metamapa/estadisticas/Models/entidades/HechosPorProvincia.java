package org.example.metamapa.estadisticas.Models.entidades;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consulta_hechos_provincia")
public class HechosPorProvincia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "cantidad_hechos")
    private long cantidad_hechos;

    public HechosPorProvincia(String titulo, String provincia, long cantidad_hechos){
        this.titulo = titulo;
        this.provincia = provincia;
        this.cantidad_hechos = cantidad_hechos;
    }

}
