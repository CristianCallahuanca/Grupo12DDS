package org.example.metamapa.gestordatos.models.dtos.input;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    private String nombre;
    private String descripcion;
    private String algoritmoConsenso; // Ej: "mayoriaSimple", "mayoriaAbsoluta", etc.
    private List<Long> idsFuentes;    // IDs de las fuentes que integran esta colección
}
