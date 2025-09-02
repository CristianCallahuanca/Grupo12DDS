package org.example.metamapa.models.dtos;

import lombok.Data;

@Data
public class FuenteConfiguradaDTO {

    private String nombre;
    private String url;
    private TipoFuente tipo;
}
