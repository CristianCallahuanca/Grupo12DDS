package org.example.metamapa.estadisticas.Models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EstadProvinciaPorCategoriaDTO {

    LocalDateTime fechaCalculo;
    String categoriaNombre;
    String provincia;
    Integer cantidadHechos;
}
