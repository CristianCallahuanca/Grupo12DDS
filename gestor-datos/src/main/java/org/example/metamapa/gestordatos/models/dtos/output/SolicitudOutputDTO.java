package org.example.metamapa.gestordatos.models.dtos.output;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;

@Data
@Getter
@Setter
public class SolicitudOutputDTO {
    private EstadoEliminar estado; // "aprobada", "denegada", "pendiente", etc.
    private String justificacion;
    private Long idHechoAsociado;
}
