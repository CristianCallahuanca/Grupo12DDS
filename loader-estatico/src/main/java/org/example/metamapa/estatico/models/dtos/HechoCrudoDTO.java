package org.example.metamapa.estatico.models.dtos;

import lombok.Getter;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;

import java.util.List;

@Getter
public class HechoCrudoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    //
    private String etiqueta;
    private String contribuyenteID;
    private List<String> archivosMultimedia;
    private String fechaCarga;
    private String id_hecho;

    public HechoCrudoDTO(HechoCrudo hecho) {
        this.titulo = hecho.getTitulo();
        this.descripcion = hecho.getDescripcion();
        this.categoria = hecho.getCategoria();
        this.latitud = hecho.getLatitud();
        this.longitud = hecho.getLongitud();
        this.fechaAcontecimiento = hecho.getFechaAcontecimiento();
        this.etiqueta = null;
        this.contribuyenteID = null;
        this.archivosMultimedia = null;
        this.fechaCarga = null;
        this.id_hecho = null;
    }
}
