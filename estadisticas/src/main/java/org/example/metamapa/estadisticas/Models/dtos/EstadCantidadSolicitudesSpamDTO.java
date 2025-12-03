package org.example.metamapa.estadisticas.Models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EstadCantidadSolicitudesSpamDTO {

    LocalDateTime fechaCalculo;
    Integer cantidadSpam;
}
