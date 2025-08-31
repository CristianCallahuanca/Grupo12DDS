package org.example.metamapa.estatico.models.dtos;

import lombok.Getter;
import org.example.metamapa.estatico.models.entidades.HechoCrudo;

@Getter
public class HechoCrudoDTO {
    private final String titulo;
    private final String descripcion;
    private final String categoria;
    private final String latitud;
    private final String longitud;
    private final String fechaAcontecimiento;

    public HechoCrudoDTO(HechoCrudo hecho) {
        this.titulo = hecho.getTitulo();
        this.descripcion = hecho.getDescripcion();
        this.categoria = hecho.getCategoria();
        this.latitud = hecho.getLatitud();
        this.longitud = hecho.getLongitud();
        this.fechaAcontecimiento = hecho.getFechaAcontecimiento();
    }
}
