package org.example.metamapa.gestordatos.models.dtos.output;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;

@Data
public class SolicitudOutputDTO {
    private EstadoEliminar estado;
    private String justificacion;
    private Long idHechoAsociado;
}
