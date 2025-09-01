package org.example.metamapa.publica.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HechoInputDTO {
    private String descripcion;
    private String tipo;
    private String fuente;
    private LocalDate fecha;
    private String ubicacion;

}
