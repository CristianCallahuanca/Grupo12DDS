package org.example.metamapa.gestordatos.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HechoFiltroInput {

    private Double sur;
    private Double oeste;
    private Double norte;
    private Double este;

    private String titulo;
    private String descripcion;
    private String categoria;

    private Boolean contieneMultimedia;

    private String desdeAcontecimiento;
    private String hastaAcontecimiento;
    private String desdeCarga;
    private String hastaCarga;

    private String estadoDeseado;
    private String coleccionId;
    private String modo;

    // getters y setters
}
