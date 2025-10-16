package org.example.metamapa.gestordatos.models.dtos.output;

import lombok.Data;
import org.example.metamapa.gestordatos.models.entidades.enums.Origen;

import java.util.List;

@Data
public class ColeccionOutputDTO {
    private String nombre;
    private String descripcion;
    private String algoritmo;
    private String handle;

    //private List<HechoOutputDTO> hechos;

}
