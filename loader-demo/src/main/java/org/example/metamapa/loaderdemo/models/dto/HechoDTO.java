package org.example.metamapa.loaderdemo.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String etiqueta;
    private LocalDate fechaAcontecimiento;
    private String contribuyenteID;
    private List<String> archivosMultimedia;
    private String origen;
    private String tipoFuente;
}

