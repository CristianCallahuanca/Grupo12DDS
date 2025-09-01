package org.example.metamapa.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HechoDTO {

    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
}
