package org.example.metamapa.loaderdemo.models.dto;

import lombok.Data;

@Data
public class HechoDTO_IN {

    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String etiqueta;
    private String fecha;
}
//TODO: REOGANIZAR LOS ATRIBUTOS