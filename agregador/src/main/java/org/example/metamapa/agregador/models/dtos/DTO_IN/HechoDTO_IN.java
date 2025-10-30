package org.example.metamapa.agregador.models.dtos.DTO_IN;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class HechoDTO_IN {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private String contribuyenteID;
    private List<String> archivosMultimedia;
    private String origen;
    private String tipoFuente;
}

/*
private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private String contribuyenteID;
    private List<String> archivosMultimedia;

* */
