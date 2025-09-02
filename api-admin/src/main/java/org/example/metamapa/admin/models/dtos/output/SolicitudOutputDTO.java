package org.example.metamapa.admin.models.dtos.output;

import lombok.Data;

@Data
public class SolicitudOutputDTO {
    private Long id;
    private String estado; // "aprobada", "denegada", "pendiente", etc.
    private String motivo;
    private Long idHechoAsociado;
}
