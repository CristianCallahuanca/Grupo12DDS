package org.example.metamapa.estatico.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.util.List;

@Data
@AllArgsConstructor
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    //
    private String etiqueta;
    private String contribuyenteID;
    private List<String> archivosMultimedia;
    private String origen;

}
