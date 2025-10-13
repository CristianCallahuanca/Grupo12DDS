package org.example.metamapa.loaderdemo.models.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estado_loader_demo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoLoaderDemo {

    @Id
    private String loaderId;

    private LocalDateTime fechaInicio;

    private LocalDateTime ultimaActualizacion;

    @Enumerated(EnumType.STRING)
    private EstadoInstancia estado;
}
