package org.example.metamapa.gestordatos.models.dtos.input;

import lombok.Data;

@Data
public class FuenteInputDTO {
    private Long id;            // o quizás otro campo clave como nombre
    private String nombreFuente;
}
