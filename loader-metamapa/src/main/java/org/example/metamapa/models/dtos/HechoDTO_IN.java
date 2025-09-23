package org.example.metamapa.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
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
    private Boolean sinCategorizar;
    private LocalDateTime fechaAcontecimientoPosta;
}
//TODO: Terminar de consolidar los atributos de los hechos que ingresan y los que se envian al agregador.