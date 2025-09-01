package org.example.metamapa.models.entidades;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HechoCrudo {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    //private String origen;


    public HechoCrudo(String titulo, String descripcion, String categoria, String latitud, String longitud,
                      String fechaAcontecimiento) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
    }
}
