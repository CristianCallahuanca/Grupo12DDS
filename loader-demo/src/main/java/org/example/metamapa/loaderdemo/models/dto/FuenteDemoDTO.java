package org.example.metamapa.loaderdemo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteDemoDTO {
    private Long id;
    private String nombreFuente;
    private String url;
    private boolean activa;
}