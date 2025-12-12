package org.example.metamapa.loaderdemo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteDemoDTO {
    private Long id;
    private String nombre;
    private String urlBase;
    private String pathApi;
    private Boolean activa;
    private String nombreDetectado;
    private String etiquetaDetectada;
}