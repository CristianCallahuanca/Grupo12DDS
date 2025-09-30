package org.example.metamapa.gestordatos.models.dtos.input;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContribuyenteRegInputDTO {
    private String nombre;
    private String apellido;
    private Integer dni;
    private Integer edad;
}
