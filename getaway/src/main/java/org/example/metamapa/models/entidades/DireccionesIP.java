package org.example.metamapa.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "direcciones_ip")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DireccionesIP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direccion_ip", nullable = false)
    private String direccionIp;

}

