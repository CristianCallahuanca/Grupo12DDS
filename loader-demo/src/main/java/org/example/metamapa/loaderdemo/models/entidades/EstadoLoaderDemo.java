package org.example.metamapa.loaderdemo.models.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estado_loader_demo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoLoaderDemo {

    @Id
    private String loaderId;

    @Enumerated(EnumType.STRING)
    private EstadoInstancia estado;
}
