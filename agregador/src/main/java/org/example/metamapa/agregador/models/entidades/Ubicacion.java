package org.example.metamapa.agregador.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ubicacion")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "latitud")
    private double latitud;

    @Column(name = "longitud")
    private double longitud;

    @Column(name = "provincia")
    private String provincia;

    public Ubicacion(double latitud, double longitud, String provincia) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.provincia = provincia;
    }
}
