package org.example.metamapa.publica.models.dtos.output;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class HechoOutputDTO {
    private Long id;
    private String descripcion;
    private String tipo;
    private String fuente;
    private LocalDate fecha;
    private String ubicacion;
    private Map<String, String> atributosExtra;

}
