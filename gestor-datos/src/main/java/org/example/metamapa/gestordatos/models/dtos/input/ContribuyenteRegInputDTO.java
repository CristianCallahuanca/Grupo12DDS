package org.example.metamapa.gestordatos.models.dtos.input;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ContribuyenteRegInputDTO {
    private String nombre;
    private String apellido;
    private Integer dni;
    private Date fechaNacimiento;
    private String email;
    private String password;
}
