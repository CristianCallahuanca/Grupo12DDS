package org.example.metamapa.gestordatos.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColeccionOutputDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    private List<String> origenesReales;
    private String algoritmo;
}
