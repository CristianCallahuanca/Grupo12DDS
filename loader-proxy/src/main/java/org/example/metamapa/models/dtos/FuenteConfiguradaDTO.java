package org.example.metamapa.models.dtos;

import lombok.Data;
import org.example.metamapa.models.entidades.TipoFuente;

@Data
public class FuenteConfiguradaDTO {

    private String nombre;
    private String url;
    private TipoFuente tipo;
}
