package org.example.metamapa.gestordatos.models.dtos.input;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    private String algoritmoConsenso; // Ej: "mayoriaSimple", "mayoriaAbsoluta", etc.
    private List<Integer> idsFuentes;    // IDs de las fuentes que integran esta colección
    private List<CriterioRequest> criterios;

}
