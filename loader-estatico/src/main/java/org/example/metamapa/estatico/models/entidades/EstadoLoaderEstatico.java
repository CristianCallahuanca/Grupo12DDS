package org.example.metamapa.estatico.models.entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estado_loader_estatico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoLoaderEstatico {

    @Id
    private String loaderId;

    @Enumerated(EnumType.STRING)
    private EstadoInstancia estado;
}
