package org.example.metamapa.gestordatos.models.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HechoOutputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private List<String> archivosMultimedia;
    private String nombre_contribuyente;
    private String apellido_contribuyente;
}

