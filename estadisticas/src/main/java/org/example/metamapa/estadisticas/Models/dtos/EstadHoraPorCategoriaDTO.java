package org.example.metamapa.estadisticas.Models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EstadHoraPorCategoriaDTO {

    LocalDateTime fechaCalculo;
    String categoriaNombre;
    Integer hora;
    Integer cantidadHechos;
}
