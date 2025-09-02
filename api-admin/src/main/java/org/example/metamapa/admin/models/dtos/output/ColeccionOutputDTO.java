package org.example.metamapa.admin.models.dtos.output;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionOutputDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String algoritmoConsenso;
    private List<FuenteOutputDTO> fuentes;
}
