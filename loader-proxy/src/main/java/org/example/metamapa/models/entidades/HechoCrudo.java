package org.example.metamapa.models.entidades;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class HechoCrudo {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String ubicacion;
    private String archivosMultimedia;
    private String etiqueta;
    private String fechaAcontecimiento;
    private String fechaCarga;
    private String contribuyenteID;
    private String id_hecho;
    //
    private String latitud;
    private String longitud;

    public HechoCrudo(String titulo, String descripcion, String categoria, String ubicacion,
                      String fechaAcontecimiento) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.latitud = null;
        this.longitud = null;
    }
}
