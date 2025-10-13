package org.example.metamapa.estatico.models.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estado_loader_estatico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoLoaderEstatico {

    @Id
    private String loaderId;

    private LocalDateTime fechaInicio;

    private LocalDateTime ultimaActualizacion;

    @Enumerated(EnumType.STRING)
    private EstadoInstancia estado;
}
