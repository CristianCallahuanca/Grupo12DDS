package org.example.metamapa.models.entidades;

import lombok.Data;

@Data
public class FuenteConfigurada {
    private String nombre;
    private String url;
    private TipoFuente tipo;

    public FuenteConfigurada(String nombre, String url, TipoFuente tipo) {
        this.nombre = nombre;
        this.url = url;
        this.tipo = tipo;
    }
}
