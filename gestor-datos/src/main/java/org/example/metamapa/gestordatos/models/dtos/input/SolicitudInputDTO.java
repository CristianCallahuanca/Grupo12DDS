package org.example.metamapa.gestordatos.models.dtos.input;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;

@Getter
@Setter
public class SolicitudInputDTO {
    private Long idhecho;
    private String justificacion;
}
